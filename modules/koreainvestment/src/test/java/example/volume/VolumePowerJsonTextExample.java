package example.volume;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;
import org.json.JSONObject;

/**
 * 체결강도 정보
 * @author macle
 */
public class VolumePowerJsonTextExample {
    public static void main(String[] args) {
        KoreainvestmentPeriodDataApi api = KoreainvestmentApi.getInstance().getPeriodDataApi();
        String text = api.getVolumePowerDailyJsonText("005930","20240601","20240625");

        JSONObject object = new JSONObject(text);


        System.out.println(text);

        System.out.println(object.getJSONArray("output2").length());

    }
}
