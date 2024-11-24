package io.runon.stock.trading.data.management;

import io.runon.stock.trading.Stock;
import io.runon.trading.CountryCode;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * @author macle
 */
public interface Spot1mCandleOutParam {
    String [] getExchanges();

    CountryCode getCountryCode();

    void sleep();

    TradeCandle [] getCandles(Stock stock, String ymd);

}
