package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;
import io.runon.trading.CountryCode;

/**
 * 보유종목과 수량정보
 */
public interface StockPathLastTime {

    //캔들
    StockPathLastTime CANDLE_SPOT = new StockPathLastTimeCandleSpot();

    StockPathLastTime CANDLE_FUTURES = new StockPathLastTimeCandleFutures();

    //신용정보
    StockPathLastTime CREDIT_LOAN = new StockPathLastTimeCreditLoan();

    //대주 정보
    StockPathLastTime STOCK_LOAN = new StockPathLastTimeLoan();
    
    //공매도
    StockPathLastTime SHORT_SELLING = new StockPathLastTimeShortSelling();

    StockPathLastTime INVESTOR = new StockPathLastTimeInvestor();

    StockPathLastTime PROGRAM = new StockPathLastTimeProgram();

    StockPathLastTime VOLUME_POWER = new StockPathLastTimeVolumePower();


    long getLastTime(Stock stock, String interval);

    String getFilesDirPath(Stock stock, String interval);

    String getLastTimeFilePath(CountryCode countryCode, String interval);
}
