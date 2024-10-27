package example;

import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.CountryCode;

/**
 * @author macle
 */
public class FuturesPathExample {
    public static void main(String[] args) {
        String path = StockPaths.getFuturesCandleFilesPath("KOR_121213","1d");

        System.out.println(path);


        System.out.println(StockPaths.getFuturesCandlePath(CountryCode.KOR));
    }
}
