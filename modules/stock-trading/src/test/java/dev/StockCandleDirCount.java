package dev;

import io.runon.stock.trading.StockCandles;
import io.runon.trading.CountryCode;

/**
 * @author macle
 */
public class StockCandleDirCount {
    public static void main(String[] args) {
        System.out.println(StockCandles.getSpotCandleDirsCount(CountryCode.kOR));

    }
}
