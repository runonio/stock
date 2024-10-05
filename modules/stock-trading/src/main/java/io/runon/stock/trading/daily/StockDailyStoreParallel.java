package io.runon.stock.trading.daily;

import com.seomse.commons.parallel.ParallelArrayJob;
import com.seomse.commons.parallel.ParallelArrayWork;
import io.runon.stock.trading.Stock;
import io.runon.trading.TradingConfig;

/**
 * @author macle
 */
public class StockDailyStoreParallel {


    public static StockDailyStore []  parallelDataSet(Stock [] stocks, int beginYmd, int endYmd){

        final StockDailyStore[] array = new StockDailyStore[stocks.length];
        for (int i = 0; i <array.length ; i++) {
            array[i] = new StockDailyStore(stocks[i]);
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