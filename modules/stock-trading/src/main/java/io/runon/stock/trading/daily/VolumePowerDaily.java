package io.runon.stock.trading.daily;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.management.StockOutTimeLineJson;
import io.runon.trading.TradingGson;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 공매도 일별
 * (한국 주식 데이터 기준정보)
 * @author macle
 */
@Data
public class VolumePowerDaily implements StockOutTimeLineJson {

    public static final VolumePowerDaily [] EMPTY_ARRAY = new VolumePowerDaily[0];
    public static final Comparator<VolumePowerDaily> SORT = Comparator.comparingInt(o -> o.ymd);

    Long t ;

    int ymd;


    BigDecimal buyVolume;
    BigDecimal sellVolume;


    public static VolumePowerDaily make(String jsonStr, Stock stock){

        VolumePowerDaily daily = make(jsonStr);
        if(daily.t == null){
            daily.t = Stocks.getDailyOpenTime(stock, daily.ymd);
        }

        return daily;
    }

    public static VolumePowerDaily make(String jsonStr){

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, VolumePowerDaily.class);
    }


    @Override
    public String outTimeLineJsonText(Stock stock){
        if(t == null){
            t = Stocks.getDailyOpenTime(stock, ymd);
        }

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }


    public long getTime(){
        return t;
    }

    public void setTime(long time){
        this.t = time;
    }
}
