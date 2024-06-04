package io.runon.stock.trading.data.management;

import com.seomse.commons.config.JsonFileProperties;
import io.runon.stock.trading.ShortSellingDaily;
import io.runon.stock.trading.Stock;
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
    }

    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {

        StockDataManager dataManager = StockDataManager.getInstance();
        LoanData loanData = dataManager.getLoanData();

        ShortSellingDaily[] dailies = loanData.getShortSellingDailies(stock, beginYmd, endYmd);

        String [] lines = new String[dailies.length];
        for (int i = 0; i <lines.length ; i++) {
            lines[i] = dailies[i].outTimeLineJsonText(stock);
        }

        return lines;
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
    public String getDeletedPropertiesKey() {
        return "delisted_stocks_short_selling_1d";
    }
}
