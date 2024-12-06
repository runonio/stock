package io.runon.stock.trading.daily;

import io.runon.commons.parallel.ParallelArrayJob;
import io.runon.commons.parallel.ParallelArrayWork;
import io.runon.stock.trading.Stock;
import io.runon.trading.TradingConfig;

import java.util.Map;

/**
 * @author macle
 */
public class StockDailyStoreParallel {


    public  static StockDailyStore [] parallelDataSet(Stock [] stocks, int beginYmd, int endYmd){
        return parallelDataSet(null, null, stocks, beginYmd, endYmd);
    }

    public static StockDailyStore [] parallelDataSet(Map<String, StockDailyStore> lastStoreMap, StockDailyStoreGap dailyStoreGap, Stock [] stocks, int beginYmd, int endYmd){

        final StockDailyStore[] array = new StockDailyStore[stocks.length];
        for (int i = 0; i <array.length ; i++) {

            Stock stock = stocks[i];
            if(lastStoreMap != null){
                //과거 메모리 데이터 살리기 (하드와의 입출력 축소)
                StockDailyStore lastStore = lastStoreMap.get(stock.getStockId());
                if(lastStore != null){
                    lastStore.setStock(stock);
                    array[i] = lastStore;
                }else{
                    array[i] = new StockDailyStore(stock);
                }
            }else{
                array[i] = new StockDailyStore(stock);
            }

            array[i].setDayGap(dailyStoreGap);
        }

        ParallelArrayWork<StockDailyStore> work = dailyAnalysis -> {
          dailyAnalysis.setData(beginYmd, endYmd);
        };

        ParallelArrayJob<StockDailyStore> parallelArrayJob = new ParallelArrayJob<>(array, work);
        parallelArrayJob.setThreadCount(TradingConfig.getTradingThreadCount());

        parallelArrayJob.runSync();

        return array;

    }
}