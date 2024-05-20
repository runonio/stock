package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;

/**
 * 보유종목과 수량정보
 */
public interface StockPathLastTime {

    //캔들
    StockPathLastTime CANDLE = new StockPathLastTimeCandle();

    //신용정보
    StockPathLastTime CREDIT_LOAN = new StockPathLastTimeCreditLoan();

    long getLastTime(Stock stock, String interval);

}
