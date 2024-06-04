package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;
import io.runon.trading.CountryCode;
import io.runon.trading.data.csv.CsvTimeFile;

import java.nio.file.FileSystems;

/**
 * @author macle
 */
public class StockPathLastTimeCandle implements StockPathLastTime{
    @Override
    public long getLastTime(Stock stock, String interval) {
        String filesDirPath = StockPaths.getSpotCandleFilesPath(stock.getStockId(),interval);
        return CsvTimeFile.getLastTime(filesDirPath);
    }

    @Override
    public String getFilesDirPath(Stock stock, String interval) {

        return StockPaths.getSpotCandleFilesPath(stock.getStockId(),interval);
    }

    @Override
    public String getLastTimeFilePath(String interval) {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return StockPaths.getSpotCandlePath(CountryCode.KOR)+fileSeparator+"candle_last_" + interval;
    }


}
