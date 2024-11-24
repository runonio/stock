package example.overseas;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentOverseasPeriodApi;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;

/**
 * @author macle
 */
public class OverseasDailyCandleJsonApi {
    public static void main(String[] args) {
        Stock stock = Stocks.getStock("USA_AAPL");

        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentOverseasPeriodApi periodApi  = api.getOverseasPeriodApi();

        System.out.println(periodApi.getDailyJsonText(stock.getExchange(), stock.getSymbol(), "20241112", true));
    }

}
