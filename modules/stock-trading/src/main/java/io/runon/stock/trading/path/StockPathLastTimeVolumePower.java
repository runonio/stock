package io.runon.stock.trading.path;

import io.runon.stock.trading.Stock;
import io.runon.trading.CountryCode;
import io.runon.trading.data.json.JsonTimeFile;

import java.nio.file.FileSystems;

/**
 * 주식 분석에 사용하는 경료확인용 유틸성 클래스
 * @author macle
 */
public class StockPathLastTimeVolumePower implements StockPathLastTime{
    @Override
    public long getLastTime(Stock stock, String interval) {
        return JsonTimeFile.getLastTime(getFilesDirPath(stock,interval));
    }

    @Override
    public String getFilesDirPath(Stock stock, String interval) {
        return StockPaths.getVolumePowerFilesPath(stock.getStockId(), interval);
    }

    @Override
    public String getLastTimeFilePath(CountryCode countryCode, String interval) {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return StockPaths.getVolumePowerPath(countryCode)+fileSeparator+"volume_power_last_" + interval;
    }
}
