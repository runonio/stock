package example;

import com.seomse.commons.config.Config;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.StockData;
import io.runon.stock.trading.data.StockDataManager;

/**
 * @author macle
 */
public class KorStocksExample {
    public static void main(String[] args) {

        Config.getConfig("");

//        KOSPI
//        KOSDAQ
//        KONEX
//        NYSE
//        NASDAQ
//        NYSE_AMEX
//        CME
//         CBOT
//        NYMEX
//        COMEX
//        CFD
//        SGX
//        NSE

        String [] exchanges = {
            "KOSPI"
            , "KOSDAQ"
        };


//        ETN
//        STOCK
//        STOCK_PREFERRED
//        STOCK_REITs
//        ETF

        String [] types = {
                "STOCK"
                , "ETF"
        };

        StockDataManager stockDataManager = StockDataManager.getInstance();
        StockData stockData = stockDataManager.getStockData();

        Stock[] stocks =stockData.getStocks(exchanges, types);

        for(Stock stock : stocks){
            System.out.println(stock);
        }

        System.out.println(stocks.length);

    }
}
