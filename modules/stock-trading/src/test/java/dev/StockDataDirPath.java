package dev;

import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.CountryCode;

/**
 * @author macle
 */
public class StockDataDirPath {
    public static void main(String[] args) {
        System.out.println(StockPaths.getSpotCandlePath(CountryCode.KOR));
        System.out.println(StockPaths.getSpotCreditLoanPath(CountryCode.KOR));
    }
}
