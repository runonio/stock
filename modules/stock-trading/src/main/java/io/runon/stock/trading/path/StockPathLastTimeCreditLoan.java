package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;
import io.runon.trading.CountryCode;
import io.runon.trading.data.json.JsonTimeFile;

import java.nio.file.FileSystems;

/**
 * 신용
 * @author macle
 */
public class StockPathLastTimeCreditLoan implements StockPathLastTime{
    @Override
    public long getLastTime(Stock stock, String interval) {
        return JsonTimeFile.getLastTime(getFilesDirPath(stock,interval));
    }

    @Override
    public String getFilesDirPath(Stock stock, String interval) {
        return StockPaths.getSpotCreditLoanFilesPath(stock.getStockId(),interval);
    }

    @Override
    public String getLastTimeFilePath(String interval) {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return StockPaths.getSpotCreditLoanPath(CountryCode.KOR)+fileSeparator+"credit_loan_last_" + interval;

    }


}