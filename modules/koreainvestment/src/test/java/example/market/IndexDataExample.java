package example.market;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentMarketApi;

/**
 * @author macle
 */
public class IndexDataExample {
    public static void main(String[] args) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentMarketApi marketApi = api.getMarketApi();

        System.out.println(marketApi.getIndexDataJsonText("KOSPI","D","19900101"));

    }
}
