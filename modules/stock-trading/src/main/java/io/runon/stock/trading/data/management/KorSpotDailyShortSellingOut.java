package io.runon.stock.trading.data.management;

import io.runon.commons.config.JsonFileProperties;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.daily.ShortSellingDaily;
import io.runon.stock.trading.data.LoanData;
import io.runon.stock.trading.data.StockDataManager;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.data.file.PathTimeLine;

/**
 * 공매도
 * @author macle
 */
public class KorSpotDailyShortSellingOut  extends KorSpotDailyOut{

    public KorSpotDailyShortSellingOut() {
        super(StockPathLastTime.SHORT_SELLING, PathTimeLine.JSON);
        dailyOut.setServiceName("short_selling");
    }

    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {

        StockDataManager dataManager = StockDataManager.getInstance();
        LoanData loanData = dataManager.getLoanData();

        ShortSellingDaily[] dailies = loanData.getShortSellingDailies(stock, beginYmd, endYmd);
        return StockOutTimeLineJson.getLines(stock,dailies);
    }

    @Override
    public void sleep() {
        try{
            Thread.sleep(1000);
        }catch (Exception ignore){}
    }

    @Override
    public int getNextDay() {
        return 2000;
    }

    @Override
    public JsonFileProperties getJsonFileProperties() {
        return StockDataManager.getInstance().getJsonFileProperties();
    }

    @Override
    public String getDelistedPropertiesKey() {
        return "delisted_stocks_short_selling_1d";
    }
}
