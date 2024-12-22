package example.investor;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;
import io.runon.stock.trading.data.daily.ProgramDaily;

/**
 * 예제
 * @author macle
 */
public class ProgramTradingDataDailyExample {
    public static void main(String[] args) {
        KoreainvestmentPeriodDataApi api = KoreainvestmentApi.getInstance().getPeriodDataApi();
        ProgramDaily[] dailies = api.getProgramDailies("005930","20240604","20240624");
        for(ProgramDaily daily : dailies){
            System.out.println(daily);
        }
    }
}
