package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;
import io.runon.trading.data.csv.CsvTimeFile;

/**
 * @author macle
 */
public class StockPathLastTimeCandle implements StockPathLastTime{
    @Override
    public long getLastTime(Stock stock, String interval) {
        String filesDirPath = StockPaths.getSpotCandleFilesPath(stock.getStockId(),interval);
        return CsvTimeFile.getLastTime(filesDirPath);
    }
}
