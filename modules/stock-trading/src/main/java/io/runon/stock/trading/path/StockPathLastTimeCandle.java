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
    public long getLastTime(Stock stock, String exchange, String interval) {
        return CsvTimeFile.getLastTime(getFilesDirPath(stock, exchange, interval));
    }

    @Override
    public String getFilesDirPath(Stock stock, String exchange, String interval) {
        return StockPaths.getSpotCandleFilesPath(stock.getStockId(),exchange,interval);
    }

    @Override
    public String getLastTimeFilePath(CountryCode countryCode, String exchange, String interval) {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return StockPaths.getSpotCandlePath(countryCode, exchange)+fileSeparator+"candle_last_" + interval;
    }
}
