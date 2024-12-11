package io.runon.stock.securities.firm.api.kor.ls;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.daily.StockInvestorDaily;
import io.runon.stock.trading.data.management.StockOutTimeLineJson;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.data.file.PathTimeLine;

/**
 * ls증권 open api
 * @author macle
 */
public class LsSpotDailyInvestorOut extends LsStockDailyOut {


    private final LsPeriodDataApi periodDataApi;

    public LsSpotDailyInvestorOut() {
        super(StockPathLastTime.INVESTOR, PathTimeLine.JSON);
        dailyOut.setServiceName("ls_investor");
        periodDataApi = api.getPeriodDataApi();
    }

    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {
        StockInvestorDaily[] dailies = periodDataApi.getInvestorDailies(stock.getSymbol(), beginYmd, endYmd);
        return StockOutTimeLineJson.getLines(stock,dailies);
    }

    @Override
    public int getNextDay() {
        return 100;
    }

    @Override
    public String getDelistedPropertiesKey() {
        return "delisted_stocks_investor_1d";
    }
}
