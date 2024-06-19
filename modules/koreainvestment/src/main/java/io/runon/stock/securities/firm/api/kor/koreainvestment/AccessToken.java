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
