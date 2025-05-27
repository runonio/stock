package example.period;

import io.runon.stock.securities.firm.api.kor.koreainvestment.SpotDailyCandleOut;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;

/**
 * @author macle
 */
public class KorStockCandleOut {
    public static void main(String[] args) {

        String [] stockIds = {
                "KOR_252670"
                , "KOR_122630"
        };

        SpotDailyCandleOut candleOut = new SpotDailyCandleOut();

        for(String stockId : stockIds){
            Stock stock = Stocks.getStock(stockId);
            candleOut.out(stock, null);
        }
        //특정 종목만 아웃
    }
}
