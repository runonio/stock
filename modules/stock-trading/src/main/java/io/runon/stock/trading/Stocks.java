package io.runon.stock.trading;

import io.runon.stock.trading.data.StockData;
import io.runon.stock.trading.data.StockDataManager;

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

}
