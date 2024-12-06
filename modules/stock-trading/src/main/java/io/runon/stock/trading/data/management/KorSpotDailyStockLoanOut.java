package io.runon.stock.trading.data.management;

import io.runon.commons.config.JsonFileProperties;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.daily.StockLoanDaily;
import io.runon.stock.trading.data.LoanData;
import io.runon.stock.trading.data.StockDataManager;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.data.file.PathTimeLine;

/**
 * @author macle
 */
public class KorSpotDailyStockLoanOut extends KorSpotDailyOut{
    public KorSpotDailyStockLoanOut() {
        super(StockPathLastTime.STOCK_LOAN, PathTimeLine.JSON);
        dailyOut.setServiceName("stock_loan");
    }

    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {

        StockDataManager dataManager = StockDataManager.getInstance();
        LoanData loanData = dataManager.getLoanData();
        StockLoanDaily[] dailies = loanData.getStockLoanDailies(stock, beginYmd, endYmd);
        return StockOutTimeLineJson.getLines(stock, dailies);
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
