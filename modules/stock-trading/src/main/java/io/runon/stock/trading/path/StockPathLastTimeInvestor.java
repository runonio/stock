package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;
import io.runon.trading.CountryCode;
import io.runon.trading.data.json.JsonTimeFile;

import java.nio.file.FileSystems;
/**
 * 보유종목과 수량정보
 */
public class StockPathLastTimeInvestor  implements StockPathLastTime{
    @Override
    public long getLastTime(Stock stock, String exchange, String interval) {
        return JsonTimeFile.getLastTime(getFilesDirPath(stock, exchange , interval));
    }

    @Override
    public String getFilesDirPath(Stock stock, String exchange, String interval) {
        return StockPaths.getInvestorFilesPath(stock.getStockId(), interval);
    }

    @Override
    public String getLastTimeFilePath(CountryCode countryCode, String exchange, String interval) {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return StockPaths.getStockLoanPath(CountryCode.KOR)+fileSeparator+"stock_investor_last_" + interval;

    }
}