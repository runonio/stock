package io.runon.stock.securities.firm.api.kor.ls;

import com.google.gson.JsonObject;
import io.runon.commons.config.Config;
import io.runon.commons.config.JsonFileProperties;
import io.runon.commons.config.JsonFilePropertiesManager;
import io.runon.commons.http.HttpApi;
import io.runon.commons.http.HttpApiResponse;
import io.runon.commons.utils.time.Times;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * ls증권 open api
 * @author macle
 */
@Slf4j
public class LsApi {
    private static class Singleton {
        private static final LsApi instance = new LsApi();
    }

    public static LsApi getInstance(){
        return Singleton.instance;
    }


    private final Object accessTokenLock = new Object();

    //실전 투자
    private final JsonFileProperties jsonFileProperties;

    final HttpApi  httpPost;

    private final LsPeriodDataApi periodDataApi;
    private final LsFuturesApi futuresApi;


    private final Object trCodeLock = new Object();

    private final Map<String, Map<String, String>> trCodeRequestPropertyMap = new HashMap<>();


    private long periodSleep = Config.getLong("stock.securities.firm.kor.ls.period.out.time", 1000L);


    private LsApi(){

        String jsonPropertiesName = "securities_firm_kor_ls.json";
        jsonFileProperties = JsonFilePropertiesManager.getInstance().getByName(jsonPropertiesName);

        String domain = Config.getConfig("stock.securities.firm.api.kor.ls.domain","https://openapi.ls-sec.co.kr:8080");

        JsonObject lastAccessTokenObj = jsonFileProperties.getJsonObject("last_access_token");
        if(lastAccessTokenObj != null){
            accessToken = new AccessToken(lastAccessTokenObj);
        }

        httpPost = new HttpApi();
        httpPost.setDefaultMethod("POST");
        httpPost.setReadTimeOut((int) Times.MINUTE_1);
        httpPost.setDefaultRequestProperty(makeRequestProperty());
        httpPost.setDefaultAddress(domain);

        periodDataApi = new LsPeriodDataApi(this);
        futuresApi = new LsFuturesApi(this);
    }

    AccessToken accessToken = null;

    public Map<String, String> makeRequestProperty(){
        Map<String, String> map = new HashMap<>();
        map.put("content-type","application/json; charset=utf-8");
        if(accessToken != null) {
            map.put("authorization", accessToken.getAuthorization());
        }

        return map;
    }


    public void updateAccessToken(){
        synchronized (accessTokenLock) {

            if(accessToken != null && accessToken.isValid()){
                return;
            }

            accessToken = AccessToken.make();
            httpPost.setRequestProperty("authorization", accessToken.getAuthorization());

            JsonObject tokenObject = accessToken.getJsonObject();
            jsonFileProperties.set("last_access_token", tokenObject);

        }
    }


    public void changeAccessToken(){
        synchronized (accessTokenLock) {
            accessToken = AccessToken.make();
            httpPost.setRequestProperty("authorization", accessToken.getAuthorization());

            JsonObject tokenObject = accessToken.getJsonObject();
            jsonFileProperties.set("last_access_token", tokenObject);

        }
    }



    public boolean isAccessTokenUpdate(HttpApiResponse response){

        try {
            String message = response.getMessage();
            JSONObject object = new JSONObject(message);
            String rspCd = object.getString("rsp_cd");
            String msg ="";
            if(!object.isNull("rsp_msg")) {
                msg = object.getString("rsp_msg");
            }
            if(rspCd.equals("IGW00121")|| rspCd.equals("IGW00123") || msg.contains("token")) {
                periodSleep();
                synchronized (accessTokenLock) {

                    accessToken = AccessToken.make();
                    String authorization = accessToken.getAuthorization();
                    httpPost.setRequestProperty("authorization", accessToken.getAuthorization());

                    JsonObject tokenObject = accessToken.getJsonObject();
                    jsonFileProperties.set("last_access_token", tokenObject);
                }

                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }


    public Map<String,String> computeIfAbsenttTrCodeMap(String trCode){
        Map<String, String> map = trCodeRequestPropertyMap.get(trCode);
        if(map != null){
            return map;
        }

        synchronized (trCodeLock){
            map = trCodeRequestPropertyMap.get(trCode);
            if(map != null){
                return map;
            }
            map = new HashMap<>();
            map.put("tr_cd", trCode);
            map.put("tr_cont", "N");
            trCodeRequestPropertyMap.put(trCode, map);

            return map;
        }
    }

    public LsPeriodDataApi getPeriodDataApi() {
        return periodDataApi;
    }

    public LsFuturesApi getFuturesApi() {
        return futuresApi;
    }

    public HttpApi getHttpPost() {
        return httpPost;
    }

    public JsonFileProperties getJsonFileProperties() {
        return jsonFileProperties;
    }

    public void periodSleep(){
        try {
            Thread.sleep(periodSleep);
        }catch (Exception ignore){}
    }


    public void setPeriodSleep(long periodSleep) {
        this.periodSleep = periodSleep;
    }
}
