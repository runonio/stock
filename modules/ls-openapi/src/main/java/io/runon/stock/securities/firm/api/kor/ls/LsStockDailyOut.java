package io.runon.stock.securities.firm.api.kor.ls;

import io.runon.commons.config.JsonFileProperties;
import io.runon.stock.trading.data.management.KorSpotDailyOut;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CountryCode;
import io.runon.trading.data.Markets;
import io.runon.trading.data.file.PathTimeLine;

/**
 * ls증권 open api
 * @author macle
 */
public abstract class LsStockDailyOut extends KorSpotDailyOut {

    protected final LsApi api = LsApi.getInstance();

    protected String [] exchanges = Markets.getDefaultExchanges(CountryCode.KOR);



    public LsStockDailyOut(StockPathLastTime stockPathLastTime, PathTimeLine pathTimeLine) {
        super(stockPathLastTime, pathTimeLine);

    }

    public void setMarkets(String[] exchanges) {
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
