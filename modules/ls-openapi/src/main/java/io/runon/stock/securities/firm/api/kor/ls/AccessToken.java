package io.runon.stock.securities.firm.api.kor.ls;

import com.google.gson.JsonObject;
import io.runon.commons.config.Config;
import io.runon.commons.exception.TokenException;
import io.runon.commons.http.HttpApiResponse;
import io.runon.commons.http.HttpApis;
import io.runon.commons.utils.GsonUtils;
import io.runon.commons.utils.time.Times;
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

        if(expiresIn > Times.HOUR_6){
            //토근 제한시간이 도지 않았음에도 만료되는 현상 발견
            //초큰 최대 교체 시간을 6시간으로 설정
            expiresIn = Times.HOUR_6;
        }

        //토큰 갱신시간 여유를 위해 1분정도전에는 갱신받음
        accessToken.expiredTime = requestTime + expiresIn - Times.MINUTE_10;

        return accessToken;
    }

}
