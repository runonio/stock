package example.price;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;

/**
 * @author macle
 */
public class Candle1mJsonExample {
    public static void main(String[] args) {
        //apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock-quotations2#L_9fece97b-401f-4379-9e9d-4365b63c1126
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();

        KoreainvestmentPeriodDataApi dataApi = api.getPeriodDataApi();
//20231107
        String text = dataApi.get1mCandleJsonText("005930", "J","20241115","2200");
        System.out.println(text);


    }
}
