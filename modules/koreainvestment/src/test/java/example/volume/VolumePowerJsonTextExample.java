package example.volume;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;
import org.json.JSONObject;

/**
 * 일별 프로그램 매매동향 내리기
 * @author macle
 */
public class VolumePowerJsonTextExample {
    public static void main(String[] args) {
        KoreainvestmentPeriodDataApi api = KoreainvestmentApi.getInstance().getPeriodDataApi();
        String text = api.getVolumePowerDailyJsonText("005930","19990101","20000101");

        JSONObject object = new JSONObject(text);


        System.out.println(text);

        System.out.println(object.getJSONArray("output2").length());

    }
}
