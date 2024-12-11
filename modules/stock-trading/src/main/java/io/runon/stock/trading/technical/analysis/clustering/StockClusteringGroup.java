package io.runon.stock.trading.technical.analysis.clustering;

import io.runon.trading.BigDecimals;
import io.runon.trading.TimeChangePercent;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author macle
 */
public class StockClusteringGroup {

    public final static Comparator<StockClusteringGroup> GROUP_SORT_DESC = (o1, o2) -> o2.dataArray[0].groupScore.compareTo(o1.dataArray[0].groupScore);


    public final static Comparator<StockClusteringData> SORT_DESC = (o1, o2) -> o2.groupScore.compareTo(o1.groupScore);

    StockClusteringData [] dataArray;
    Map<String, StockClusteringData> dataMap;

    public void sort(){
        if(dataArray.length == 1){
            return;
        }

        //군집내 점수를 더한다.
        //전체 타임라인 구하기
        //군집내 모든 시간라인 정보생성
        Map<Long, List<BigDecimal>> timeChangeMap = new HashMap<>();
        for(StockClusteringData data : dataArray){
            TimeChangePercent [] timeChanges = data.getChangeArray();
            for(TimeChangePercent timeChange : timeChanges){
                List<BigDecimal> list = timeChangeMap.computeIfAbsent(timeChange.getTime(), k -> new ArrayList<>());
                list.add(timeChange.getChangePercent());
            }
        }


        Map<Long, BigDecimal> avgMap = new HashMap<>();


        Set<Long> keys = timeChangeMap.keySet();
        for(Long key : keys){
            List<BigDecimal> list = timeChangeMap.get(key);
            avgMap.put(key, BigDecimals.average(list));

        }

        for(StockClusteringData data : dataArray){
            data.setGroupScore(avgMap);
        }

        Arrays.sort(dataArray, SORT_DESC);

    }


    public StockClusteringData[] getDataArray() {
        return dataArray;
    }
}
