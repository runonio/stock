package io.runon.stock.trading.group;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockMap;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.StockData;
import io.runon.stock.trading.data.StockDataManager;

/**
 * @author macle
 */
public class StockGroups {

    public static Stock[] getGroupStocks(StockMap stockMap, String groupId) {
        StockData stockData = StockDataManager.getInstance().getStockData();
        String [] groupStockIds = stockData.getGroupStockIds(groupId);

        return Stocks.getStocks(stockMap, groupStockIds);
    }

    public static  Stock [] getGroupStocks( String groupId) {
        StockData stockData = StockDataManager.getInstance().getStockData();
        return stockData.getGroupStocks(groupId);
    }
}
