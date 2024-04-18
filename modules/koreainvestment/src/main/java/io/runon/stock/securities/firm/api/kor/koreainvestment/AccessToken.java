package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.google.gson.JsonObject;
import com.seomse.commons.exception.ParseRuntimeException;
import com.seomse.commons.utils.GsonUtils;
import com.seomse.commons.utils.time.Times;
import io.runon.trading.TradingTimes;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 파일처리 관련 유틸성 클래스
 * @author macle
 */
@Data
public class AccessToken {
//    {
//    "access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2tlbiIsImF1ZCI6ImI2YTMwOWM1LWMzYjMtNDI1NC05ZjEwLTFlNmEwNGJiMTU1NSIsImlzcyI6InVub2d3IiwiZXhwIjoxNzEwMTc3NTI3LCJpYXQiOjE3MTAwOTExMjcsImp0aSI6IlBTVDc0NTNRNTRtNVhLZmJmbHNvRldiZnVnYU1acWtFckVReiJ9.S1Rfw3d36N1nqhmofTKIh39k9Sw9mGUlrWcmOoUGEnfSnxJc4duusTtZz711YdN9o9cYIcfr6h7lqqtbnakYow",
//    "access_token_token_expired":"2024-03-12 02:18:47",
//    "token_type":"Bearer","expires_in":86400}

    public AccessToken(){

    }

    public AccessToken(String jsonText){
        set(GsonUtils.fromJsonObject(jsonText));
    }

    public AccessToken(JsonObject jsonObject){
        set(jsonObject);
    }

    public void set(JsonObject jsonObject){
        token = jsonObject.get("access_token").getAsString();
        type =  jsonObject.get("token_type").getAsString();
        String expiredTimeText = jsonObject.get("access_token_token_expired").getAsString();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone(TradingTimes.KOR_ZONE_ID));
        // 10분미리 종료시켜서 새로발급
        try {
            expiredTime = format.parse(expiredTimeText).getTime() - Times.MINUTE_10;
        } catch (ParseException e) {
            throw new ParseRuntimeException(e);
        }
    }

    String token;
    String type;
    //만료시간
    long expiredTime;

    public String getAuthorization(){
        return type +" " + token;
    }

    public boolean isValid(){
        return System.currentTimeMillis() < expiredTime;
    }
}
