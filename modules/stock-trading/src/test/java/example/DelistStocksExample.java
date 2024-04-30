package example;

import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.Stock;
/**
 * @author macle
 */
public class DelistStocksExample {
    public static void main(String[] args) {
        String [] exchanges = {
                "KOSPI"
                , "KOSDAQ"
        };

        Stock [] stocks = Stocks.getDelistedStocks(exchanges, "19900101", "20240430");

        for(Stock stock : stocks){
            System.out.println(stock);
        }
        System.out.println(stocks.length);

    }
}
