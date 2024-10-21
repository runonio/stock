package example.futures;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentFuturesApi;

/**
 *
 * @author macle
 */
public class FuturesPeriodExample {
    public static void main(String[] args) {
        KoreainvestmentFuturesApi futuresApi = KoreainvestmentApi.getInstance().getFuturesApi();
        String jsonText =futuresApi.getPeriodFuturesJsonText("JF", "111S06", "D", "20210401","20211231");
        System.out.println(jsonText);
    }
}
