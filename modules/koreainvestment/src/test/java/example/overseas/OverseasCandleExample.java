package example.overseas;

import io.runon.stock.securities.firm.api.kor.koreainvestment.UasSpotDailyCandleOut;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;

/**
 * @author macle
 */
public class OverseasCandleExample {
    public static void main(String[] args) {
        Stock stock = Stocks.getStock("USA_AAPL");

        UasSpotDailyCandleOut candleOut = new UasSpotDailyCandleOut();
        candleOut.out(stock);

    }
}
