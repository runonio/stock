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


}
