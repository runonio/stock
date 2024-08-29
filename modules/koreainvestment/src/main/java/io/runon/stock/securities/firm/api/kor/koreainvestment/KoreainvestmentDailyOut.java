package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.config.JsonFileProperties;
import io.runon.stock.trading.data.management.KorSpotDailyOut;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CountryCode;
import io.runon.trading.data.Exchanges;
import io.runon.trading.data.file.PathTimeLine;

/**
 * 현물 일봉 캔들 내리기
 * 한국 투자증권은 아직 분봉과거치를 지원하지 않음
 * 올해 지원예정 중이라고 하였음
 * @author macle
 */
public abstract class KoreainvestmentDailyOut extends KorSpotDailyOut {

    protected final KoreainvestmentApi koreainvestmentApi = KoreainvestmentApi.getInstance();


    protected final KoreainvestmentPeriodDataApi periodDataApi = koreainvestmentApi.getPeriodDataApi();

    protected String [] exchanges = Exchanges.getDefaultExchanges(CountryCode.KOR);

    public KoreainvestmentDailyOut(StockPathLastTime stockPathLastTime, PathTimeLine pathTimeLine){
        super(stockPathLastTime, pathTimeLine);

    }


    public void setExchanges(String[] exchanges) {
        this.exchanges = exchanges;
    }

    @Override
    public JsonFileProperties getJsonFileProperties() {
        return koreainvestmentApi.getJsonFileProperties();
    }


    @Override
    public void sleep() {
        koreainvestmentApi.periodSleep();
    }


}
