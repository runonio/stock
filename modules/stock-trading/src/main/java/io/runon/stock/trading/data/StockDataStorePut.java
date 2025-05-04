package io.runon.stock.trading.data;
/**
 * @author macle
 */
public interface StockDataStorePut {

    void setData(StockDataStore dataStore, String exchange, int beginYmd, int endYmd);
}
