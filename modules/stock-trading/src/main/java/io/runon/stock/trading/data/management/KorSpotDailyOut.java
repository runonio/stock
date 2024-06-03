package io.runon.stock.trading.data.management;

import io.runon.stock.trading.Exchanges;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.PathTimeLine;

/**
 * @author macle
 */
public abstract class KorSpotDailyOut implements StockDailyOutParam {

    protected String [] exchanges = Exchanges.getDefaultExchanges(CountryCode.KOR);

    protected final PathTimeLine pathTimeLine;

    protected final StockPathLastTime stockPathLastTime;

    protected SpotDailyOut dailyOut;

    public KorSpotDailyOut(StockPathLastTime stockPathLastTime, PathTimeLine pathTimeLine){
        this.stockPathLastTime = stockPathLastTime;
        this.pathTimeLine = pathTimeLine;
        dailyOut = new SpotDailyOut(this);
        dailyOut.setZoneId(TradingTimes.KOR_ZONE_ID);
    }


    public void setExchanges(String[] exchanges) {
        this.exchanges = exchanges;
    }


    public void outKor(){
        dailyOut.out();
    }

    //상폐된 주식 캔들 내리기
    public void outKorDelisted(){
        dailyOut.outDelisted();
    }

    /**
     * 상장 시점부터 내릴 수 있는 전체 정보를 내린다.
     * @param stock 종목정보
     */
    public void out(Stock stock){
        dailyOut.out(stock);
    }


    @Override
    public String[] getExchanges() {
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
