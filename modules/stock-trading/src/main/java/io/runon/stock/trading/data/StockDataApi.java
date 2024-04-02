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
    public Stock[] getStocks(String[] exchanges, String[] types) {
        return new Stock[0];
    }

    @Override
    public Stock[] getStocks(String[] exchanges) {
        return new Stock[0];
    }
}
