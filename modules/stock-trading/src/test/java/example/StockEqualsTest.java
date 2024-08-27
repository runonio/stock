package example;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.trading.data.jdbc.TradingJdbc;

/**
 * @author macle
 */
public class StockEqualsTest {
    public static void main(String[] args) {

        Stock stock = Stocks.getStock("KOR_500069");

        Stock stock2 = Stocks.getStock("KOR_500069");
        stock2.setUpdatedAt(System.currentTimeMillis());

        System.out.println(TradingJdbc.equalsOutUpdatedAt(stock, stock2));


    }
}
