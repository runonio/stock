package io.runon.stock.trading.data.management;

import com.seomse.commons.config.JsonFileProperties;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.StockDataManager;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.data.file.PathTimeLine;

/**
 * @author macle
 */
public class KorSpotDailyStockLoanOut extends KorSpotDailyOut{
    public KorSpotDailyStockLoanOut(StockPathLastTime stockPathLastTime, PathTimeLine pathTimeLine) {
        super(stockPathLastTime, pathTimeLine);
    }

    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {



        return new String[0];
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
        return "delisted_stocks_loan_1d";
    }
}