package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.commons.utils.time.YmdUtils;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.management.Spot1mCandleOut;
import io.runon.stock.trading.data.management.Spot1mCandleOutParam;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.Markets;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * @author macle
 */
public class UsaSpot1mCandleOut implements Spot1mCandleOutParam {

    protected String [] exchanges = Markets.getDefaultExchanges(CountryCode.USA);

    protected final KoreainvestmentApi koreainvestmentApi = KoreainvestmentApi.getInstance();
    protected final KoreainvestmentOverseasPeriodApi dataApi;

    protected final Spot1mCandleOut candleOut;

    public UsaSpot1mCandleOut(){
        dataApi = koreainvestmentApi.getOverseasPeriodApi();
        candleOut = new Spot1mCandleOut(this);
        candleOut.setZoneId(TradingTimes.USA_ZONE_ID);
    }

    public void out(){
        String exchange = null;
        out(exchange);
    }

    public void out(String exchange){
        candleOut.out(YmdUtils.getYmd(YmdUtils.now(TradingTimes.USA_ZONE_ID), -45), exchange);
    }


    public void out(Stock stock){
        out(stock, null);
    }

    public void out(Stock stock, String exchange){
        candleOut.out(YmdUtils.getYmd(YmdUtils.now(TradingTimes.USA_ZONE_ID), -45), stock, exchange);
    }


    @Override
    public String[] getExchanges() {
        return exchanges;
    }

    @Override
    public CountryCode getCountryCode() {
        return CountryCode.USA;
    }

    @Override
    public void sleep() {
        koreainvestmentApi.minuteSleep();
    }

    @Override
    public TradeCandle[] getCandles(Stock stock, String ymd) {
        return dataApi.get1mCandles(stock, ymd);
    }
}
