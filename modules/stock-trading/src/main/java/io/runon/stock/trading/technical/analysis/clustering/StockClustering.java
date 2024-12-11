package io.runon.stock.trading.technical.analysis.clustering;

import io.runon.commons.data.BigDecimalArray;
import io.runon.commons.data.IdArray;
import io.runon.commons.parallel.ParallelArrayJob;
import io.runon.commons.parallel.ParallelArrayWork;
import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.similarity.TradingSimilarity;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
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

    private final StockClusteringData[] dataArray;

    private String interval = "1d";

    private long beginTime = -1;
    private long endTime = -1;

    private BigDecimal inPercentGap = new BigDecimal("0.2");

    private int minSize = 20;

    private BigDecimal minSimPercent = new BigDecimal("80");


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


    public void setTime(long beginTime, long endTime){
        this.beginTime =beginTime;
        this.endTime = endTime;
    }

    public void setInPercentGap(BigDecimal inPercentGap) {
        this.inPercentGap = inPercentGap;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
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

        //데이터 메모리 로드 실행
        //데이터 로드 실행
        ParallelArrayWork<StockClusteringData> work = stockSimCandle -> {
            stockSimCandle.setBeginTime(beginTime);
            stockSimCandle.setEndTime(endTime);
            stockSimCandle.setInterval(interval);
            stockSimCandle.getChangeArray();
            //평균값 생성
            stockSimCandle.getAvgChange();
        };

        runJobSync(work);
        
        //유사도 검색 실행
        //work 재구현
        work = stockSimCandle -> {
            stockSimCandle.searchArray =  TradingSimilarity.search(stockSimCandle, dataArray, inPercentGap, minSize, minSimPercent);
        };
        //유사도 검색 결과 대입
        runJobSync(work);
        
        //유사도 검색결과중 가장 많이 군집된것으로 합침
        Arrays.sort(dataArray, StockClusteringData.SORT_ASC);

        Map<String, StockClusteringData> dataMap = new HashMap<>();
        Set<String> checkSet = new HashSet<>();

        for(StockClusteringData data : dataArray){
            dataMap.put(data.getId(), data);
        }

        List<StockClusteringGroup> groupList = new ArrayList<>();

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
            stockClusteringGroup.sort();
        };

        ParallelArrayJob<StockClusteringGroup> parallelArrayJob = new ParallelArrayJob<>(groups, groupWork);
        parallelArrayJob.setThreadCount(threadCount);
        parallelArrayJob.runSync();

        Arrays.sort(groups, StockClusteringGroup.GROUP_SORT_DESC);

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
            checkSet.add(searchData.getId());
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

    public void outGroupJson(String outPath){



    }


}