package io.runon.stock.trading;

import com.seomse.commons.parallel.ParallelArrayJob;
import com.seomse.commons.parallel.ParallelArrayWork;
import io.runon.stock.trading.data.StockData;
import io.runon.stock.trading.data.StockDataManager;
import io.runon.stock.trading.data.StockLong;
import io.runon.stock.trading.exception.StockNotSupportedException;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingConfig;

import java.nio.file.FileSystems;
import java.util.Arrays;

/**
 * @author macle
 */
public class Stocks {

    public static Stock getStock(String id){
        StockDataManager stockDataManager = StockDataManager.getInstance();
        StockData stockData = stockDataManager.getStockData();
        return stockData.getStock(id);
    }

    public static Stock[] getStocks(String [] exchanges, String [] types){
        StockDataManager stockDataManager = StockDataManager.getInstance();
        StockData stockData = stockDataManager.getStockData();
        return stockData.getStocks(exchanges, types);
    }

    public static Stock[] getStocks(String [] exchanges){
        StockDataManager stockDataManager = StockDataManager.getInstance();
        StockData stockData = stockDataManager.getStockData();
        return stockData.getStocks(exchanges);
    }

    public static Stock [] getDelistedStocks(String[] exchanges, String beginYmd, String endYmd){
        StockDataManager stockDataManager = StockDataManager.getInstance();
        StockData stockData = stockDataManager.getStockData();
        return stockData.getDelistedStocks(exchanges, beginYmd, endYmd);
    }

    public static String getCountryCode(String stockId){
        return stockId.substring(0, stockId.indexOf("_"));
    }

    public static String [] getIds(Stock [] stocks){
        String [] ids = new String[stocks.length];
        for (int i = 0; i <ids.length ; i++) {
            ids[i] = stocks[i].getStockId();
        }
        return ids;
    }

    public static void sortUseLastTimeParallel(Stock [] stocks, String interval, StockPathLastTime stockPathLastTime){

        String fileSeparator = FileSystems.getDefault().getSeparator();
        StockLong[] sortStocks = new StockLong[stocks.length];
        for (int i = 0; i <sortStocks.length ; i++) {
            Stock stock = stocks[i];
            sortStocks[i] = new StockLong();
            sortStocks[i].setStock(stock);
        }

        ParallelArrayWork<StockLong> work = stockLong -> {

            long time = stockPathLastTime.getLastTime(stockLong.getStock(), interval);
            stockLong.setNum(time);
        };

        ParallelArrayJob<StockLong> parallelArrayJob = new ParallelArrayJob<>(sortStocks, work);
        parallelArrayJob.setThreadCount(TradingConfig.getTradingThreadCount());

        parallelArrayJob.runSync();

        Arrays.sort(sortStocks, StockLong.SORT);
        for (int i = 0; i <sortStocks.length ; i++) {
            stocks[i] = sortStocks[i].getStock();
        }
    }




}
