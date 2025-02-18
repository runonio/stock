package io.runon.stock.securities.firm.api.kor.koreainvestment;

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
public class KorSpot1mCandleOut implements Spot1mCandleOutParam {

    protected String [] exchanges = Exchanges.getDefaultExchanges(CountryCode.KOR);

    protected final KoreainvestmentApi koreainvestmentApi = KoreainvestmentApi.getInstance();
    protected final KoreainvestmentPeriodDataApi dataApi;

    protected final Spot1mCandleOut candleOut;

    public KorSpot1mCandleOut(){
        dataApi = koreainvestmentApi.getPeriodDataApi();
        candleOut = new Spot1mCandleOut(this);
        candleOut.setZoneId(TradingTimes.KOR_ZONE_ID);
    }

    public void out(){
        candleOut.out("20231107");
    }

    public void out(Stock stock){
        candleOut.out("20231107", stock);
    }


    @Override
    public String[] getExchanges() {
        return exchanges;
    }

    @Override
    public CountryCode getCountryCode() {
        return CountryCode.KOR;
    }

    @Override
    public void sleep() {
        koreainvestmentApi.minuteSleep();
    }

    @Override
    public TradeCandle[] getCandles(Stock stock, String ymd) {
        return dataApi.get1mCandles(stock.getSymbol(), ymd);
    }
}
