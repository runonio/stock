package io.runon.stock.securities.firm.api.kor.ls;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockInvestorDaily;
import io.runon.stock.trading.data.management.StockOutTimeLineJson;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.data.file.PathTimeLine;

/**
 * ls증권 open api
 * @author macle
 */
public class SpotDailyInvestorOut extends LsDailyOut{
    public SpotDailyInvestorOut() {
        super(StockPathLastTime.INVESTOR, PathTimeLine.JSON);
    }

    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {
        LsApi lsApi = LsApi.getInstance();
        LsPeriodDataApi periodDataApi = lsApi.getPeriodDataApi();
        StockInvestorDaily[] dailies = periodDataApi.getInvestorDailies(stock.getSymbol(), beginYmd, endYmd);
        return StockOutTimeLineJson.getLines(stock,dailies);
    }

    @Override
    public int getNextDay() {
        return 100;
    }

    @Override
    public String getDeletedPropertiesKey() {
        return "delisted_stocks_investor_1d";
    }
}
