package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.config.JsonFileProperties;
import io.runon.stock.trading.Exchanges;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.management.SpotDailyOut;
import io.runon.stock.trading.data.management.StockDailyOutParam;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.PathTimeLine;

/**
 * 현물 일봉 캔들 내리기
 * 한국 투자증권은 아직 분봉과거치를 지원하지 않음
 * 올해 지원예정 중이라고 하였음
 * @author macle
 */
public abstract class KoreainvestmentDailyOut implements StockDailyOutParam {

    protected final KoreainvestmentApi koreainvestmentApi = KoreainvestmentApi.getInstance();


    protected final KoreainvestmentPeriodDataApi periodDataApi = koreainvestmentApi.getPeriodDataApi();

    protected String [] exchanges = Exchanges.getDefaultExchanges(CountryCode.KOR);

    protected final PathTimeLine pathTimeLine;

    protected final StockPathLastTime stockPathLastTime;

    protected SpotDailyOut dailyOut;

    public KoreainvestmentDailyOut(StockPathLastTime stockPathLastTime, PathTimeLine pathTimeLine){
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
    public JsonFileProperties getJsonFileProperties() {
        return koreainvestmentApi.getJsonFileProperties();
    }


    @Override
    public void sleep() {
        koreainvestmentApi.periodSleep();
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
