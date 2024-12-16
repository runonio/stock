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

    public void sort(Map<Long, BigDecimal> avgMap){

        for(StockClusteringData data : dataArray){
            data.setGroupScore(avgMap);
        }


        if(dataArray.length == 1){
            return;
        }

        sort();

    }


    public void sort(){
        Arrays.sort(dataArray, SORT_DESC);
    }


    public StockClusteringData[] getDataArray() {
        return dataArray;
    }
}
