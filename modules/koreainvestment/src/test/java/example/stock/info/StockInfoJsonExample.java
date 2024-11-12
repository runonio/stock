package example.stock.info;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentStockInfoApi;

/**
 * 예제
 * @author macle
 */
public class StockInfoJsonExample {
    public static void main(String[] args) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentStockInfoApi stockInfoApi = api.getStockInfoApi();


        System.out.println(stockInfoApi.getStockInfoJsonText("005930"));
    }
}
