package io.runon.stock.trading.data.management;

import io.runon.commons.config.JsonFileProperties;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.data.file.PathTimeLine;

/**
 * @author macle
 */
public interface StockDailyOutParam {

    String [] getLines(Stock stock, String beginYmd, String endYmd );

    void sleep();

    PathTimeLine getPathTimeLine();

    StockPathLastTime getStockPathLastTime();

    String [] getMarkets();

    int getNextDay();

    JsonFileProperties getJsonFileProperties();

    String getDelistedPropertiesKey();

}