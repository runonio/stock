package io.runon.stock.trading.data;

import io.runon.stock.trading.Stock;
/**
 * @author macle
 */
public interface StockData {

    Stock getStock(String id);

    /**
     * 상장 폐지 제외
     */
    Stock[] getStocks(String [] markets, String [] types);

    /**
     * 상장 폐지 제외
     */
    Stock[] getStocks(String [] markets);

    /**
     * 상장 폐지 제외
     */
    Stock[] getStocks(String [] markets, String baseYmd);

    Stock[] getDelistedStocks(String [] markets, String beginYmd, String endYmd);

    /**
     * 상장폐지 포함
     */
    Stock[] getAllStocks(String [] markets, String [] types);


    Stock[] getAllStocks(String [] markets);

    String [] getGroupStockIds(String groupId);

    Stock [] getGroupStocks(String groupId);

}
