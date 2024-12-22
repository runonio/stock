package io.runon.stock.trading.data;

import io.runon.commons.parallel.ParallelArrayJob;
import io.runon.commons.parallel.ParallelArrayWork;
import io.runon.stock.trading.Stock;
import io.runon.trading.TradingConfig;

import java.util.Map;

/**
 * @author macle
 */
public class StockDataStoreParallel {


    public  static StockDataStore[] parallelDataSet(Stock [] stocks, StockDataStoreParam stockDailyStoreParam, Stock[] stocks1, int beginYmd, int endYmd){
        return parallelDataSet(null, null, stocks, beginYmd, endYmd, null);
    }

    public static StockDataStore[] parallelDataSet(Map<String, StockDataStore> lastStoreMap, StockDataStoreParam dailyStoreParam, Stock [] stocks, int beginYmd, int endYmd, StockDataStorePut put){

        final StockDataStore[] array = new StockDataStore[stocks.length];
        for (int i = 0; i <array.length ; i++) {

            Stock stock = stocks[i];
            if(lastStoreMap != null){
                //과거 메모리 데이터 살리기 (하드와의 입출력 축소)
                StockDataStore lastStore = lastStoreMap.get(stock.getStockId());
                if(lastStore != null){
                    lastStore.setStock(stock);
                    array[i] = lastStore;
                }else{
                    array[i] = new StockDataStore(stock);
                }
            }else{
                array[i] = new StockDataStore(stock);
            }

            array[i].setDayGap(dailyStoreParam);
        }

        ParallelArrayWork<StockDataStore> work = dataStore -> {
          dataStore.setData(beginYmd, endYmd);
          if(put != null){
              put.setData(dataStore, beginYmd, endYmd);
          }
        };

        ParallelArrayJob<StockDataStore> parallelArrayJob = new ParallelArrayJob<>(array, work);
        parallelArrayJob.setThreadCount(TradingConfig.getTradingThreadCount());

        parallelArrayJob.runSync();

        return array;

    }
}