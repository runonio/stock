package io.runon.stock.trading.technical.analysis.clustering;

import com.google.gson.JsonArray;
import io.runon.commons.data.BigDecimalArray;
import io.runon.commons.data.IdArray;
import io.runon.commons.parallel.ParallelArrayJob;
import io.runon.commons.parallel.ParallelArrayWork;
import io.runon.commons.utils.FileUtil;
import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.trading.*;
import io.runon.trading.technical.analysis.similarity.TradingSimilarity;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;

/**
 * 기본은 1일봉을 활용
 * 기본값 기간은 오늘부터 365일로 한다.
 * 모든 기능 에는 설정없이 동작할 수 있는 기본값을 반드시 고민한다.
 * getGroups() 결과를 호출해서 결과를 확인할 수 있다.
 *
 * @author macle
 */
@Slf4j
public class StockClustering implements Runnable{


    public enum Type{
        CONTINUE_MERGE //연속 합치기
        , SEARCH_RANK //검색하여 최고 랭크 순으로 생성
    }

    private Type type = Type.CONTINUE_MERGE;


    int minCount = -1;

    private StockClusteringData[] dataArray;

    private String interval = "1d";

    private long beginTime = -1;
    private long endTime = -1;

    private BigDecimal inPercentGap = new BigDecimal("0.2");


    private BigDecimal minSimPercent = new BigDecimal("80");

    BigDecimal minAmount = new BigDecimal("100000000");


    private StockClusteringGroup [] groups;

    public StockClustering(StockClusteringData[] dataArray){
        this.dataArray = dataArray;
    }

    public StockClustering(Stock [] stocks){

        this.dataArray = new StockClusteringData[stocks.length];
        for (int i = 0; i < dataArray.length ; i++) {
            dataArray[i] = new StockClusteringData(stocks[i]);
        }
    }

    private int threadCount = TradingConfig.getTradingThreadCount();

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }


    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setTime(long beginTime, long endTime){
        this.beginTime =beginTime;
        this.endTime = endTime;
    }

    public void setYmd(int beginYmd, int endYmd, ZoneId zoneId){
        //begin time은 0시, endtime은 24시 이므로 day1을 더한다,
        this.beginTime =YmdUtil.getTime(beginYmd, zoneId);
        this.endTime = YmdUtil.getTime(endYmd, zoneId) + Times.DAY_1;
    }

    public void setYmd(String beginYmd, String endYmd, ZoneId zoneId){
        //begin time은 0시, endtime은 24시 이므로 day1을 더한다,
        this.beginTime =YmdUtil.getTime(beginYmd, zoneId);
        this.endTime = YmdUtil.getTime(endYmd, zoneId) + Times.DAY_1;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public void setInPercentGap(BigDecimal inPercentGap) {
        this.inPercentGap = inPercentGap;
    }

    public void setMinSimPercent(BigDecimal minSimPercent) {
        this.minSimPercent = minSimPercent;
    }

    @Override
    public void run(){
        if(beginTime < 0){
            beginTime = YmdUtil.nowInt(TradingTimes.KOR_ZONE_ID);
        }

        if(endTime < 0){
            endTime = beginTime - Times.YEAR_1;
            if(endTime < 0){
                endTime = 0;
            }
        }

        final List<StockClusteringData> validList = new ArrayList<>();
        final Object addLock = new Object();
        //데이터 메모리 로드 실행
        //데이터 로드 실행

        final StockClustering parents = this;

        ParallelArrayWork<StockClusteringData> work = stockClusteringData -> {
            stockClusteringData.setBeginTime(beginTime);
            stockClusteringData.setEndTime(endTime);
            stockClusteringData.setInterval(interval);
            stockClusteringData.getChangeArray();
            //평균값 생성
            stockClusteringData.getAvgChange();

        };

        runJobSync(work);


        Map<Long, List<BigDecimal>> timeChangeMap = new HashMap<>();
        for(StockClusteringData data : dataArray){
            TimeChangePercent[] timeChanges = data.getChangeArray();
            for(TimeChangePercent timeChange : timeChanges){
                List<BigDecimal> list = timeChangeMap.computeIfAbsent(timeChange.getTime(), k -> new ArrayList<>());
                list.add(timeChange.getChangePercent());
            }
        }
        final Map<Long, BigDecimal> avgMap = new HashMap<>();
        Set<Long> keys = timeChangeMap.keySet();
        for(Long key : keys){
            List<BigDecimal> list = timeChangeMap.get(key);
            avgMap.put(key, BigDecimals.average(list));

        }

        if(minCount < 1){
            minCount = avgMap.size()/3;
        }

        for(StockClusteringData data : dataArray){
            if(data.isValid(this)) {
                validList.add(data);
            }
        }

        //유효성이 검사된 데이터로 배열 재정의
        dataArray = validList.toArray(new StockClusteringData[0]);

        validList.clear();

        //유사도 검색 실행
        //work 재구현
        work = stockClusteringData -> {
            stockClusteringData.searchArray =  TradingSimilarity.search(stockClusteringData, dataArray, inPercentGap, minCount, minSimPercent, minAmount);
        };
        //유사도 검색 결과 대입
        runJobSync(work);

        Map<String, StockClusteringData> dataMap = new HashMap<>();
        Set<String> checkSet = new HashSet<>();

        for(StockClusteringData data : dataArray){
            dataMap.put(data.getId(), data);
        }

        List<StockClusteringGroup> groupList = new ArrayList<>();

        if(type == Type.CONTINUE_MERGE){

            //유사도 검색결과중 가장 많이 군집된것으로 합침
            Arrays.parallelSort(dataArray, StockClusteringData.SORT_ASC);

            for(StockClusteringData data : dataArray){
                if(checkSet.contains(data.getId())){
                    continue;
                }

                List<StockClusteringData> groupDataList = new ArrayList<>();
                addGroupData(data, groupDataList, dataMap, checkSet);
                if(!groupDataList.isEmpty()){
                    //그룹추가
                    StockClusteringGroup group = new StockClusteringGroup();

                    group.dataArray = groupDataList.toArray(new StockClusteringData[0]);
                    group.dataMap = dataMap;
                    groupList.add(group);

                    groupDataList.clear();
                }
            }

            groups = groupList.toArray(new StockClusteringGroup[0]);
            //군집결과 생성

            ParallelArrayWork<StockClusteringGroup> groupWork = stockClusteringGroup -> {
                stockClusteringGroup.sort(avgMap);
            };

            ParallelArrayJob<StockClusteringGroup> parallelArrayJob = new ParallelArrayJob<>(groups, groupWork);
            parallelArrayJob.setThreadCount(threadCount);
            parallelArrayJob.runSync();

            Arrays.parallelSort(groups, StockClusteringGroup.GROUP_SORT_DESC);
            Arrays.parallelSort(dataArray, StockClusteringData.GROUP_SCORE_DESC);

        }else if(type == Type.SEARCH_RANK){
            work = stockClusteringData -> {
                stockClusteringData.setGroupScore(avgMap);
            };

            runJobSync(work);

            Arrays.parallelSort(dataArray, StockClusteringData.GROUP_SCORE_DESC);

            for(StockClusteringData data : dataArray){
                if(checkSet.contains(data.getId())){
                    continue;
                }

                List<StockClusteringData> groupDataList = new ArrayList<>();

                groupDataList.add(data);

                checkSet.add(data.getId());

                IdArray<BigDecimalArray>[] searchArray = data.getSearchArray();

                for(IdArray<BigDecimalArray> array : searchArray){
                    groupDataList.add(dataMap.get(array.getId()));
                    checkSet.add(data.getId());
                }

                    //그룹추가
                StockClusteringGroup group = new StockClusteringGroup();

                group.dataArray = groupDataList.toArray(new StockClusteringData[0]);
                group.dataMap = dataMap;
                group.sort();
                groupList.add(group);


                groupDataList.clear();



            }
            groups = groupList.toArray(new StockClusteringGroup[0]);

        }
    }

    public void addGroupData(StockClusteringData data, List<StockClusteringData> list,  Map<String, StockClusteringData> dataMap,  Set<String> checkSet){
        if(data == null){
            return;
        }

        if(checkSet.contains(data.getId())){
            return;
        }
        checkSet.add(data.getId());

        list.add(data);

        IdArray<BigDecimalArray> [] searchArray = data.getSearchArray();
        for(IdArray<BigDecimalArray> searchData: searchArray){
            if(checkSet.contains(searchData.getId())){
                continue;
            }
            StockClusteringData nextData = dataMap.get(searchData.getId());
            addGroupData(nextData,list,dataMap,checkSet);
        }
    }

    private void runJobSync(ParallelArrayWork<StockClusteringData> work){
        ParallelArrayJob<StockClusteringData> parallelArrayJob = new ParallelArrayJob<>(dataArray, work);
        parallelArrayJob.setThreadCount(threadCount);
        parallelArrayJob.runSync();
    }

    /**
     * 군집결과
     */
    public StockClusteringGroup[] getGroups(){
        return groups;
    }

    public StockClusteringData[] getDataArray() {
        return dataArray;
    }

    public void outGroupJson(String outPath){
        outGroupJson(outPath,null);
    }


    public StockClusteringData [] getRankStocks(BigDecimal minScore){
//        int size = (int)(dataArray.length * rate);
//        List<BigDecimal> list = new ArrayList<>();
//
//        for (int i = 0; i < size; i++) {
//            list.add(dataArray[i].getGroupScore());
//        }
//        BigDecimal avg = BigDecimals.average(list);

        Set<String> checkset = new HashSet<>();

        List<StockClusteringData> dataList = new ArrayList<>();

        for(StockClusteringGroup group :  groups ){
            StockClusteringData data = group.getDataArray()[0];
            if(data.groupScore.compareTo(minScore) < 0){
                continue;
            }

            if(checkset.contains(data.getId())){
                continue;
            }

            dataList.add(data);
            checkset.add(data.getId());
        }
        StockClusteringData [] array = dataList.toArray(new StockClusteringData[0]);

        Arrays.sort(array, StockClusteringData.GROUP_SCORE_DESC);

        dataList.clear();
        return array;
    }

    public void outGroupJson(String outPath, BigDecimal minScore){
        JsonArray jsonArray = new JsonArray();
        for(StockClusteringGroup group :  groups ){

            if(minScore != null){
                if(group.getDataArray()[0].groupScore.compareTo(minScore) < 0){
                    continue;
                }
            }

            JsonArray groupJson = new JsonArray();
            StockClusteringData[] dataArray = group.getDataArray();
            for(StockClusteringData data: dataArray){
                groupJson.add(data.getCsvString());
            }
            jsonArray.add(groupJson);
        }

        FileUtil.fileOutput(TradingGson.PRETTY.toJson(jsonArray),outPath,false);
    }


}