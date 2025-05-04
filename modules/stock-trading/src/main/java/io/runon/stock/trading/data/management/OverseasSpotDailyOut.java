package io.runon.stock.trading.data.management;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CountryCode;
import io.runon.trading.CountryUtils;
import io.runon.trading.data.Exchanges;
import io.runon.trading.data.file.PathTimeLine;

/**
 * 해외주식 일별정보 내리기
 * @author macle
 */
public abstract class OverseasSpotDailyOut implements StockDailyOutParam {

    protected String [] exchanges;

    protected final PathTimeLine pathTimeLine;

    protected final StockPathLastTime stockPathLastTime;

    protected SpotDailyOut dailyOut;

    public OverseasSpotDailyOut(CountryCode countryCode, StockPathLastTime stockPathLastTime, PathTimeLine pathTimeLine){
        this.stockPathLastTime = stockPathLastTime;
        this.pathTimeLine = pathTimeLine;

        this.exchanges = Exchanges.getDefaultExchanges(countryCode);
        dailyOut = new SpotDailyOut(this);
        dailyOut.setZoneId(CountryUtils.getZoneId(countryCode));
        dailyOut.setCountryCode( countryCode);
    }


    public void setExchanges(String[] exchanges) {
        this.exchanges = exchanges;
    }

    public void out(){
        dailyOut.out(null);
    }

    public void out(Stock stock){
        dailyOut.out(stock, null);
    }

    public void reOut(Stock stock){
        dailyOut.reOut(stock, null);
    }


    @Override
    public String[] getMarkets() {
        return exchanges;
    }


    @Override
    public StockPathLastTime getStockPathLastTime() {
        return stockPathLastTime;
    }

    @Override
    public PathTimeLine getPathTimeLine() {
        return pathTimeLine;
    }

}
