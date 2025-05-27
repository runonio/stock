package example.stock.info;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentStockInfoApi;

/**
 * 예제
 * @author macle
 */
public class OrderBookExample {
    public static void main(String[] args) {

        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentStockInfoApi stockInfoApi = api.getStockInfoApi();

        String jsonText = stockInfoApi.getOrderBookJsonText("005930","NXT");
        System.out.println(jsonText);

    }
}
