package example;

import com.seomse.commons.config.Config;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.trading.CountryCode;

/**
 * @author macle
 */
public class StockCandlePrintExample {
    public static void main(String[] args) {

        Config.getConfig("");

        Stock stock = Stocks.getStock(CountryCode.KOR, "453850");

        System.out.println(stock);



//        Stocks.getDailyCandles()
    }
}
