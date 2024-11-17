package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.management.Spot1mCandleOut;
import io.runon.stock.trading.data.management.Spot1mCandleOutParam;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.Exchanges;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * @author macle
 */
public class UsaSpot1mCandleOut implements Spot1mCandleOutParam {

    protected String [] exchanges = Exchanges.getDefaultExchanges(CountryCode.USA);

    protected final KoreainvestmentApi koreainvestmentApi = KoreainvestmentApi.getInstance();
    protected final KoreainvestmentOverseasPeriodApi dataApi;

    protected final Spot1mCandleOut candleOut;

    public UsaSpot1mCandleOut(){
        dataApi = koreainvestmentApi.getOverseasPeriodApi();
        candleOut = new Spot1mCandleOut(this);
        candleOut.setZoneId(TradingTimes.USA_ZONE_ID);
    }

    public void out(){
        candleOut.out(YmdUtil.getYmd(YmdUtil.now(TradingTimes.USA_ZONE_ID), -45));
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
