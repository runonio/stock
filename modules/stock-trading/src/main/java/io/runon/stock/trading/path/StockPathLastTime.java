package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;

/**
 * 보유종목과 수량정보
 */
public interface StockPathLastTime {


    StockPathLastTime CANDLE = new StockPathLastTimeCandle();

    long getLastTime(Stock stock, String interval);

}
