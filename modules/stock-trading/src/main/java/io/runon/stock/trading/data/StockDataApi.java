package io.runon.stock.trading.data;

import io.runon.stock.trading.Stock;
/**
 * @author macle
 */
public class StockDataApi implements StockData{
    @Override
    public Stock getStock(String id) {
        return null;
    }

    @Override
    public Stock[] getStocks(String[] markets, String[] types) {
        return new Stock[0];
    }

    @Override
    public Stock[] getStocks(String[] markets) {
        return new Stock[0];
    }

    @Override
    public Stock[] getStocks(String[] markets, String baseYmd) {
        return new Stock[0];
    }

    @Override
    public Stock[] getDelistedStocks(String[] markets, String beginYmd, String endYmd) {
        return new Stock[0];
    }

    @Override
    public Stock[] getAllStocks(String[] markets, String [] types) {
        return new Stock[0];
    }

    @Override
    public Stock[] getAllStocks(String[] markets) {
        return new Stock[0];
    }

    @Override
    public String[] getGroupStockIds(String groupId) {
        return new String[0];
    }

    @Override
    public Stock[] getGroupStocks(String groupId) {
        return new Stock[0];
    }
}
