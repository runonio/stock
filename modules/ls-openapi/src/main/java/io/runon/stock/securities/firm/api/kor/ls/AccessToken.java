package io.runon.stock.securities.firm.api.kor.ls;

import com.google.gson.JsonObject;
import com.seomse.commons.config.Config;
import com.seomse.commons.exception.TokenException;
import com.seomse.commons.http.HttpApiResponse;
import com.seomse.commons.http.HttpApis;
import com.seomse.commons.utils.GsonUtils;
import com.seomse.commons.utils.time.Times;
import io.runon.trading.TradingGson;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 파일처리 관련 유틸성 클래스
 * @author macle
 */
@Data
public class AccessToken {


    public AccessToken(){

    }

    public AccessToken(String jsonText){
        set(GsonUtils.fromJsonObject(jsonText));
    }

    public AccessToken(JsonObject jsonObject){
        set(jsonObject);
    }

    public void set(JsonObject jsonObject){
        token = jsonObject.get("token").getAsString();
        scope =  jsonObject.get("scope").getAsString();
        type = jsonObject.get("type").getAsString();
        expiredTime = jsonObject.get("expired_time").getAsLong();
    }

    String token;
    String scope;
    String type;
    //만료시간
    long expiredTime;

    public String getAuthorization(){

        return type +" " + token;

    }

    public boolean isValid(){
        return System.currentTimeMillis() < expiredTime;
    }

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }

    public JsonObject getJsonObject(){
        String jsonText = toString();
        return GsonUtils.fromJsonObject(jsonText);
    }

    public static AccessToken make(){

        Map<String, String> map = new HashMap<>();
        map.put("content-type","application/x-www-form-urlencoded");

        String appKey = Config.getConfig("stock.securities.firm.api.kor.ls.key");
        String secretKey = Config.getConfig("stock.securities.firm.api.kor.ls.secret.key");
        String param = "appkey=" + appKey
                + "&appsecretkey=" + secretKey
                + "&grant_type=client_credentials"
                + "&scope=oob";

        long requestTime = System.currentTimeMillis();
        String domain = Config.getConfig("stock.securities.firm.api.kor.ls.domain","https://openapi.ls-sec.co.kr:8080");
        HttpApiResponse httpResponse = HttpApis.post(domain+ "/oauth2/token", map, param);

        if(httpResponse.getResponseCode() != 200){
            throw new TokenException("token make fail code:" + httpResponse.getResponseCode() +", " + httpResponse.getMessage());
        }

        JsonObject tokenObject = GsonUtils.fromJsonObject(httpResponse.getMessage());

        AccessToken accessToken = new AccessToken();
        accessToken.token = tokenObject.get("access_token").getAsString();
        accessToken.scope = tokenObject.get("scope").getAsString();
        accessToken.type = tokenObject.get("token_type").getAsString();

        long expiresIn = tokenObject.get("expires_in").getAsLong()*1000L;

        //토큰 갱신시간 여유를 위해  2분정도는 유효시간을 줄임
        accessToken.expiredTime = requestTime + expiresIn - Times.MINUTE_2;

        return accessToken;
    }

}
