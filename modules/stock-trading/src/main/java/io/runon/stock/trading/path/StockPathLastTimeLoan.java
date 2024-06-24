package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;
import io.runon.trading.CountryCode;
import io.runon.trading.data.json.JsonTimeFile;

import java.nio.file.FileSystems;

/**
 * 대주
 * @author macle
 */
public class StockPathLastTimeLoan implements StockPathLastTime{
    @Override
    public long getLastTime(Stock stock, String interval) {
        return JsonTimeFile.getLastTime(getFilesDirPath(stock,interval));
    }

    @Override
    public String getFilesDirPath(Stock stock, String interval) {
        return StockPaths.getStockLoanFilesPath(stock.getStockId(), interval);
    }

    @Override
    public String getLastTimeFilePath(CountryCode countryCode, String interval) {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return StockPaths.getStockLoanPath(CountryCode.KOR)+fileSeparator+"stock_loan_last_" + interval;

    }
}
