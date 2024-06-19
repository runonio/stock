package io.runon.stock.securities.firm.api.kor.ls;

import com.seomse.commons.http.HttpApiResponse;
import io.runon.stock.trading.exception.StockApiException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * ls증권 open api
 * @author macle
 */
public class LsInvestorApi {

    private final LsApi lsApi;
    public LsInvestorApi(LsApi lsApi){
        this.lsApi = lsApi;
    }

    //최대 250건
    public String getInvestorDailyJson(String symbol, String beginYmd, String endYmd){
        lsApi.updateAccessToken();
        String url = "/stock/frgr-itt";
        Map<String, String> requestHeaderMap = lsApi.computeIfAbsenttTrCodeMap("t1717");

        JSONObject paramObject = new JSONObject();
        paramObject.put("shcode", symbol);
        paramObject.put("gubun", "0");
        paramObject.put("fromdt", beginYmd);
        paramObject.put("todt", endYmd);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t1717InBlock", paramObject);

        HttpApiResponse response =  lsApi.getHttpPost().getResponse(url , requestHeaderMap, jsonObject.toString());

        if(response.getResponseCode() != 200){
            throw new StockApiException("fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();
    }



    public static void main(String[] args) {
        LsApi api = LsApi.getInstance();


        String jsonText = api.getInvestorApi().getInvestorDailyJson("005930","20190101","20240618");

        JSONObject object = new JSONObject(jsonText);
        JSONArray array = object.getJSONArray("t1717OutBlock");

        System.out.println(array.length());

    }
}
