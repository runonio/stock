package example.overseas;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentOverseasPeriodApi;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;

/**
 * @author macle
 */
public class Overseas1mCandleJsonApiExample {
    public static void main(String[] args) {
        Stock stock = Stocks.getStock("USA_AAPL");

        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentOverseasPeriodApi periodApi  = api.getOverseasPeriodApi();
        //2024년 11월 16일 체크해보니 10월 11일부터제공 한달정도 제공하는걸로 보임.
        System.out.println(periodApi.get1mCandleJsonText(stock.getMarket(), stock.getSymbol(), "20241114", "1402"));
    }
}
