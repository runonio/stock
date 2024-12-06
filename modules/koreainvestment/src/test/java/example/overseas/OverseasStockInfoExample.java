package example.overseas;

import io.runon.commons.config.Config;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentOverseasStockInfoApi;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;

/**
 * @author macle
 */
public class OverseasStockInfoExample {
    public static void main(String[] args) {
        Config.getConfig("");

        Stock stock = Stocks.getStock("USA_AAPL");

        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentOverseasStockInfoApi stockInfoApi  = api.getOverseasStockInfoApi();

        System.out.println(stockInfoApi.getStockInfoJsonText(stock));

    }
}
