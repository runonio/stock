package example.overseas;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentOverseasPeriodApi;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * @author macle
 */
public class Overseas1mCandleExample {
    public static void main(String[] args) {

        Stock stock = Stocks.getStock("USA_AAPL");

        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentOverseasPeriodApi periodApi  = api.getOverseasPeriodApi();


        TradeCandle[] candles = periodApi.get1mCandles(stock,"20241114");

        for(TradeCandle candle : candles){
            System.out.println(candle);
        }

    }
}
