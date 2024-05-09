package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;
import io.runon.trading.data.json.JsonTimeFile;

/**
 * @author macle
 */
public class StockPathLastTimeCreditLoan  implements StockPathLastTime{
    @Override
    public long getLastTime(Stock stock, String interval) {
        String filesDirPath = StockPaths.getSpotCreditLoanFilesPath(stock.getStockId(),interval);
        return JsonTimeFile.getLastTime(filesDirPath);
    }
}