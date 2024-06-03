package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;

/**
 * 대주
 * @author macle
 */
public class StockPathLastTimeLoan implements StockPathLastTime{
    @Override
    public long getLastTime(Stock stock, String interval) {
        return 0;
    }

    @Override
    public String getFilesDirPath(Stock stock, String interval) {
        return null;
    }

    @Override
    public String getLastTimeFilePath(String interval) {
        return null;
    }
}
