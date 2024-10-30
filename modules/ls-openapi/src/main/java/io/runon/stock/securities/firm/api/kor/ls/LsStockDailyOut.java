package io.runon.stock.securities.firm.api.kor.ls;

import com.seomse.commons.config.JsonFileProperties;
import io.runon.stock.trading.data.management.KorSpotDailyOut;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CountryCode;
import io.runon.trading.data.Exchanges;
import io.runon.trading.data.file.PathTimeLine;

/**
 * ls증권 open api
 * @author macle
 */
public abstract class LsStockDailyOut extends KorSpotDailyOut {

    protected final LsApi api = LsApi.getInstance();

    protected String [] exchanges = Exchanges.getDefaultExchanges(CountryCode.KOR);



    public LsStockDailyOut(StockPathLastTime stockPathLastTime, PathTimeLine pathTimeLine) {
        super(stockPathLastTime, pathTimeLine);

    }

    public void setExchanges(String[] exchanges) {
        this.exchanges = exchanges;
    }

    @Override
    public JsonFileProperties getJsonFileProperties() {
        return api.getJsonFileProperties();
    }


    @Override
    public void sleep() {
        api.periodSleep();
    }

}
