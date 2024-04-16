package dev;

import io.runon.stock.trading.candle.StockCandles;
import io.runon.trading.CountryCode;

import java.nio.file.FileSystems;

/**
 * @author macle
 */
public class StockCandleDirPath {
    public static void main(String[] args) {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        String filesDirPath = StockCandles.getStockSpotCandlePath(CountryCode.kOR)+fileSeparator+"KOR_TEST"+fileSeparator+"1d";
        System.out.println(filesDirPath);
    }
}
