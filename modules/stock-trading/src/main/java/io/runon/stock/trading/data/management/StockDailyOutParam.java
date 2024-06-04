package io.runon.stock.trading.data.management;

import com.seomse.commons.config.JsonFileProperties;
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

    String [] getExchanges();

    int getNextDay();

    JsonFileProperties getJsonFileProperties();

    String getDeletedPropertiesKey();




}
