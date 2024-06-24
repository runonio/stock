package example.investor;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;

/**
 * 예제
 * @author macle
 */
public class ProgramTradingDataDailyJsonExample {
    public static void main(String[] args) {
        KoreainvestmentPeriodDataApi api = KoreainvestmentApi.getInstance().getPeriodDataApi();
        String jsonText =api.getProgramTradingDataDailyJsonText("005930","20240624");
        System.out.println(jsonText);
    }
}
