package io.runon.stock.trading;

import io.runon.stock.trading.data.StockData;
import io.runon.stock.trading.data.StockDataManager;

/**
 * @author macle
 */
public class StockGroups {

    public static Stock [] getGroupStocks(StockMap stockMap, String groupId) {
        StockData stockData = StockDataManager.getInstance().getStockData();
        String [] groupStockIds = stockData.getGroupStockIds(groupId);

        return Stocks.getStocks(stockMap, groupStockIds);
    }

    public static  Stock [] getGroupStocks( String groupId) {
        StockData stockData = StockDataManager.getInstance().getStockData();
        return stockData.getGroupStocks(groupId);
    }
}
