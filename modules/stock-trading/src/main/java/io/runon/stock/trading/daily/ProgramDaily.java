package io.runon.stock.trading.daily;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.management.StockOutTimeLineJson;
import io.runon.trading.TradingGson;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 프로그램 매매 (일별)
 * (한국 주식 데이터 기준정보)
 * @author macle
 */
@Data
public class ProgramDaily implements StockOutTimeLineJson {

    public static final ProgramDaily [] EMPTY_ARRAY = new ProgramDaily[0];

    Long t ;
    int ymd;

    //종가
    BigDecimal close;
    //일거래량
    BigDecimal volume;
    //일거래대금
    BigDecimal amount;

    BigDecimal change;

    //순매수 볼륨
    BigDecimal netBuyVolume;
    //순매수 거래대금
    BigDecimal netBuyAmount;

    //순매수 거래량 변화
    BigDecimal netBuyChangeVolume;
    //순매수 거래대금 변화
    BigDecimal netBuyChangeAmount;

    //프로그램 매도거래량
    BigDecimal sellVolume;
    //프로그램 매도 거래대금
    BigDecimal sellAmount;

    //프로그램 매수
    BigDecimal buyVolume;
    BigDecimal buyAmount;


    public long getTime(){
        return t;
    }

    public void setTime(long time){
        this.t = time;
    }


    public static ProgramDaily make(String jsonStr){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, ProgramDaily.class);
    }

    public static ProgramDaily make(String jsonStr, Stock stock){

        ProgramDaily daily = make(jsonStr);
        if(daily.t == null){
            daily.t = Stocks.getDailyOpenTime(stock, daily.ymd);
        }

        return daily;
    }
    @Override
    public String outTimeLineJsonText(Stock stock){
        if(t == null){
            t = Stocks.getDailyOpenTime(stock, ymd);
        }

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }
    public String outTimeLineJsonText(){
        if(t == null){
            throw new RuntimeException("time null");
        }
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }

}
