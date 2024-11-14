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
    Stock[] getStocks(String [] exchanges, String [] types);

    /**
     * 상장 폐지 제외
     */
    Stock[] getStocks(String [] exchanges);

    /**
     * 상장 폐지 제외
     */
    Stock[] getStocks(String [] exchanges, String baseYmd);

    Stock[] getDelistedStocks(String [] exchanges, String beginYmd, String endYmd);

    /**
     * 상장폐지 포함
     */
    Stock[] getAllStocks(String [] exchanges, String [] types);

    String [] getGroupStockIds(String groupId);

    Stock [] getGroupStocks(String groupId);

}
