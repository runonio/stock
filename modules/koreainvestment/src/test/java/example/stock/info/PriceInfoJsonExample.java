package example.stock.info;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentStockInfoApi;

/**
 * @author macle
 */
public class PriceInfoJsonExample {
    public static void main(String[] args) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentStockInfoApi stockInfoApi = api.getStockInfoApi();

        String jsonText = stockInfoApi.getPriceInfoJsonText("005930");
        System.out.println(jsonText);
    }
}
