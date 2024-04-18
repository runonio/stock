package example.price;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;

/**
 * 개별종목 일별 신용잔고
 * @author macle
 */
public class StockDailyCreditBalance {
    public static void main(String[] args) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();

        KoreainvestmentPeriodDataApi periodDataApi = api.getPeriodDataApi();
        String text = periodDataApi.getDailyCreditBalanceJson("005930","20240404");

        System.out.println( text);
    }
}
