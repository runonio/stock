package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;
import io.runon.trading.CountryCode;
import io.runon.trading.data.json.JsonTimeFile;

import java.nio.file.FileSystems;

/**
 * 공매도
 * @author macle
 */
public class StockPathLastTimeShortSelling implements StockPathLastTime{
    @Override
    public long getLastTime(Stock stock, String interval) {
        String filesDirPath = StockPaths.getShortSellingFilesPath(stock.getStockId(),interval);
        return JsonTimeFile.getLastTime(filesDirPath);
    }

    @Override
    public String getFilesDirPath(Stock stock, String interval) {
        return StockPaths.getShortSellingFilesPath(stock.getStockId(), interval);
    }

    @Override
    public String getLastTimeFilePath(String interval) {
        String fileSeparator = FileSystems.getDefault().getSeparator();

        return StockPaths.getShortSellingPath(CountryCode.KOR)+fileSeparator+"short_selling_last_" + interval;
    }
}
