package example.volume;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;
import io.runon.stock.trading.daily.VolumePowerDaily;
import org.json.JSONObject;

/**
 * 체결강도 정보
 * @author macle
 */
public class VolumePowerDailyExample {
    public static void main(String[] args) {

        KoreainvestmentPeriodDataApi api = KoreainvestmentApi.getInstance().getPeriodDataApi();
        VolumePowerDaily [] dailies = api.getVolumePowerDailies("005930","20240601","20240625");

        for(VolumePowerDaily daily : dailies){
            System.out.println(daily);
        }


    }
}
