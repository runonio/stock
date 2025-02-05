package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.google.gson.JsonObject;
import io.runon.commons.config.Config;
import io.runon.commons.config.JsonFileProperties;
import io.runon.commons.config.JsonFilePropertiesManager;
import io.runon.commons.exception.TokenException;
import io.runon.commons.http.HttpApi;
import io.runon.commons.http.HttpApiResponse;
import io.runon.commons.http.HttpApis;
import io.runon.commons.utils.GsonUtils;
import io.runon.commons.utils.time.Times;
import io.runon.trading.CountryCode;
import io.runon.trading.closed.days.ClosedDaysFileOut;

import java.util.HashMap;
import java.util.Map;

/**
 * @author macle
 */
public class KoreainvestmentApi {

    private static class Singleton {
        private static final KoreainvestmentApi instance = new KoreainvestmentApi();
    }

    public static KoreainvestmentApi getInstance(){
        return Singleton.instance;
    }

    //실전 투자
    private static final String ACTUAL_DOMAIN = "https://openapi.koreainvestment.com:9443";
    
    //모의 투자
    private static final String SIMULATED_DOMAIN ="https://openapivts.koreainvestment.com:29443";

    private final String key = Config.getConfig("stock.securities.firm.api.kor.koreainvestment.key");
    private final String secretKey = Config.getConfig("stock.securities.firm.api.kor.koreainvestment.secret.key");

    private final String customerType =  Config.getConfig("stock.securities.firm.api.kor.koreainvestment.customer.type", "P");

    private final JsonFileProperties jsonFileProperties;

    private AccessToken accessToken;

    private final String accessTokenParam;

    private final String domain;

    final HttpApi httpGet, httpPost;

    public final HttpApi [] httpApis;

    private final KoreainvestmentPeriodDataApi periodDataApi;

    private final KoreainvestmentAccountApi accountApi;

    private final KoreainvestmentMarketApi marketApi;

    private final KoreainvestmentFuturesApi futuresApi;

    private final KoreainvestmentStockInfoApi stockInfoApi;

    private final KoreainvestmentOverseasStockInfoApi overseasStockInfoApi;

    private final KoreainvestmentOverseasPeriodApi overseasPeriodApi;

    private boolean isActual ;

    private long sleepTime = Config.getLong("stock.securities.firm.kor.koreainvestment.sleep.time", 70L);

    private long periodSleepTime = Config.getLong("stock.securities.firm.kor.koreainvestment.period.out.time", 1000L);


    private long minuteSleepTime = Config.getLong("stock.securities.firm.kor.koreainvestment.minute.out.time", 300L);

    private final ClosedDaysFileOut closedDaysFileOut;

    private KoreainvestmentApi(){

        String jsonPropertiesName = "securities_firm_kor_koreainvestment.json";
        jsonFileProperties = JsonFilePropertiesManager.getInstance().getByName(jsonPropertiesName);

        isActual = Config.getBoolean("stock.securities.firm.api.kor.koreainvestment.actual", true);
        domain = getDomain(isActual);

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("grant_type","client_credentials");
        paramObject.addProperty("appkey",key);
        paramObject.addProperty("appsecret", secretKey);
        accessTokenParam = GsonUtils.toJson(paramObject);

        JsonObject lastAccessTokenObj = jsonFileProperties.getJsonObject("last_access_token");
        if(lastAccessTokenObj != null){
            accessToken = new AccessToken(lastAccessTokenObj);
        }

        httpGet = new HttpApi();
        httpGet.setDefaultMethod("GET");
        httpGet.setReadTimeOut((int)Times.MINUTE_1);
        httpGet.setDefaultRequestProperty(makeRequestProperty());
        httpGet.setDefaultAddress(domain);

        httpPost = new HttpApi();
        httpPost.setDefaultMethod("POST");
        httpPost.setReadTimeOut((int)Times.MINUTE_1);
        httpPost.setDefaultRequestProperty(makeRequestProperty());
        httpPost.setDefaultAddress(domain);

        this.httpApis = new HttpApi[]{httpGet, httpPost};

        updateAccessToken();

        periodDataApi = new KoreainvestmentPeriodDataApi(this);
        accountApi = new KoreainvestmentAccountApi(this);
        marketApi = new KoreainvestmentMarketApi(this);
        futuresApi = new KoreainvestmentFuturesApi(this);
        stockInfoApi = new KoreainvestmentStockInfoApi(this);
        overseasStockInfoApi = new KoreainvestmentOverseasStockInfoApi(this);
        overseasPeriodApi = new KoreainvestmentOverseasPeriodApi(this);
        closedDaysFileOut = new ClosedDaysFileOut(marketApi, CountryCode.KOR);
    }

    public boolean closedDaysOut(){
        return closedDaysFileOut.out();
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setActual(boolean actual) {
        isActual = actual;
    }

    public boolean isActual() {
        return isActual;
    }

    private final Object accessTokenLock = new Object();
    public void updateAccessToken(){
        synchronized (accessTokenLock) {
            if(accessToken != null && accessToken.isValid()){
                return;
            }

            HttpApiResponse httpResponse = HttpApis.postJson(domain + "/oauth2/tokenP", accessTokenParam);
            if(httpResponse.getResponseCode() != 200){
                throw new TokenException("token make fail code:" + httpResponse.getResponseCode() +", " + httpResponse.getMessage());
            }

            JsonObject tokenObject = GsonUtils.fromJsonObject(httpResponse.getMessage());
            jsonFileProperties.set("last_access_token", tokenObject);

            accessToken = new AccessToken(tokenObject);

            String authorization = accessToken.getAuthorization();

            for(HttpApi httpApi : httpApis){
                httpApi.setRequestProperty("authorization", authorization);
            }
        }
    }


    public void updateTokenNoCheck(){
        synchronized (accessTokenLock) {
            HttpApiResponse httpResponse = HttpApis.postJson(domain + "/oauth2/tokenP", accessTokenParam);
            if(httpResponse.getResponseCode() != 200){
                throw new TokenException("token make fail code:" + httpResponse.getResponseCode() +", " + httpResponse.getMessage());
            }

            JsonObject tokenObject = GsonUtils.fromJsonObject(httpResponse.getMessage());
            jsonFileProperties.set("last_access_token", tokenObject);

            accessToken = new AccessToken(tokenObject);

            String authorization = accessToken.getAuthorization();

            for(HttpApi httpApi : httpApis){
                httpApi.setRequestProperty("authorization", authorization);
            }
        }
    }

    public Map<String, String> makeRequestProperty(){
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type","application/json; charset=utf-8");
        if(accessToken != null) {
            map.put("authorization", accessToken.getAuthorization());
        }
        map.put("appkey", key);
        map.put("appsecret", secretKey);
        map.put("custtype", customerType);
        //        map.put("personalSeckey", secretKey);
//        map.put("tr_cont", " ");
//        map.put("seq_no", " ");
//        map.put("mac_address", "");
//        map.put("phone_num", "");
//        map.put("ip_addr", "");
//        map.put("hashkey", "");
//        map.put("gt_uid", "");

        return map;
    }

    public static String getDomain(boolean isActual){
        if(isActual){
            return ACTUAL_DOMAIN;
        }else{
            return SIMULATED_DOMAIN;
        }
    }

    public String getLastAccessTokenJson(){
        JsonObject lastToken  = jsonFileProperties.getJsonObject("last_access_token");
        if(lastToken == null){
            return null;
        }
        return GsonUtils.toJson(lastToken);
    }

    public HttpApi getHttpGet() {
        return httpGet;
    }

    public HttpApi getHttpPost() {
        return httpPost;
    }

    public HttpApi[] getHttpApis() {
        return httpApis;
    }

    public KoreainvestmentPeriodDataApi getPeriodDataApi() {
        return periodDataApi;
    }

    public KoreainvestmentAccountApi getAccountApi() {
        return accountApi;
    }

    public KoreainvestmentMarketApi getMarketApi() {
        return marketApi;
    }

    public KoreainvestmentFuturesApi getFuturesApi() {
        return futuresApi;
    }

    private final Map<String, Map<String, String>> urlRequestPropertyMap = new HashMap<>();
    private final Object urlRequestPropertyLock = new Object();

    public Map<String,String> computeIfAbsenttPropertySingleMap(String urlKey, String key, String value){
        Map<String, String> map = urlRequestPropertyMap.get(urlKey);
        if(map != null){
            return map;
        }

        synchronized (urlRequestPropertyLock){
            map = urlRequestPropertyMap.get(urlKey);
            if(map != null){
                return map;
            }

            map = makeSingleMap(key,value);

            urlRequestPropertyMap.put(urlKey, map);

            return map;
        }
    }

    public Map<String,String> getRequestPropertyMap(String urlKey){
        return urlRequestPropertyMap.get(urlKey);
    }

    public void putRequestPropertyMap(String urlKey, Map<String,String> map){
        synchronized (urlRequestPropertyLock){
            if(urlRequestPropertyMap.containsKey(urlKey)){
                return;
            }
            urlRequestPropertyMap.put(urlKey, map);
        }
    }

    public Map<String,String> makeSingleMap(String key, String value){
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public void sleep(){
        try {
            Thread.sleep(sleepTime);
        }catch (Exception ignore){}
    }

    public void periodSleep(){
        try {
            Thread.sleep(periodSleepTime);
        }catch (Exception ignore){}
    }

    public void minuteSleep(){
        try {
            Thread.sleep(minuteSleepTime);
        }catch (Exception ignore){}

    }

    public long getPeriodSleepTime() {
        return periodSleepTime;
    }

    public JsonFileProperties getJsonFileProperties() {
        return jsonFileProperties;
    }

    public void setPeriodSleepTime(long periodSleepTime) {
        this.periodSleepTime = periodSleepTime;
    }

    public KoreainvestmentStockInfoApi getStockInfoApi() {
        return stockInfoApi;
    }

    public KoreainvestmentOverseasStockInfoApi getOverseasStockInfoApi() {
        return overseasStockInfoApi;
    }

    public KoreainvestmentOverseasPeriodApi getOverseasPeriodApi() {
        return overseasPeriodApi;
    }
}
