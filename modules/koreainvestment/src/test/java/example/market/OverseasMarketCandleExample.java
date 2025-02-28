package example.market;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentOverseasPeriodApi;

/**
 * @author macle
 */
public class OverseasMarketCandleExample {
    public static void main(String[] args) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentOverseasPeriodApi marketApi = api.getOverseasPeriodApi();
//        overseas_index.txt 파일의 첫글자를 뺀 3글자
        System.out.println(marketApi.getMarketCandleJson("N","SPX","20250101","20250226","D"));

    }
}
