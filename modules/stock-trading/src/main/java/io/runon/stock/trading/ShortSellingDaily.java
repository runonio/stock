package io.runon.stock.trading;

import io.runon.stock.trading.data.management.StockOutTimeLineJson;
import io.runon.trading.TradingGson;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 공매도 일별
 * (한국 주식 데이터 기준정보)
 * @author macle
 */
@Data
public class ShortSellingDaily implements StockOutTimeLineJson {
    public static final ShortSellingDaily [] EMPTY_ARRAY = new ShortSellingDaily[0];

    Long t ;


    int ymd;

    //전체수량
    BigDecimal totalQty;
    //업틱룰적용
    BigDecimal uptickRuleInQty;
    BigDecimal uptickRuleOutQty;

    BigDecimal balanceQty;

    BigDecimal totalAmt;
    //업틱룰적용
    BigDecimal uptickRuleInAmt;
    BigDecimal uptickRuleOutAmt;

    BigDecimal balanceAmt;



    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }

    /**
     * 전체데이터가 채워진 날짜까지가 저장일로 사용하기 위해 전체 데이터가 세팅된 데이터인지 확이
     * @return 전체 데이터가 세팅되었는지 여부
     */
    @SuppressWarnings({"DuplicatedCode", "RedundantIfStatement"})
    public boolean isAllDataSet(){
        if(totalQty == null){
            return false;
        }
        if(uptickRuleInQty == null){
            return false;
        }
        if(uptickRuleOutQty == null){
            return false;
        }
        if(balanceQty == null){
            return false;
        }
        if(totalAmt == null){
            return false;
        }
        if(uptickRuleInAmt == null){
            return false;

        }
        if(uptickRuleOutAmt == null){
            return false;
        }
        if(balanceAmt == null){
            return false;
        }


        return true;
    }

    public long getTime(){
        return t;
    }

    public void setTime(long time){
        this.t = time;
    }


    public static ShortSellingDaily make(String jsonStr){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, ShortSellingDaily.class);
    }

    public static ShortSellingDaily make(String jsonStr, Stock stock){

        ShortSellingDaily daily = make(jsonStr);
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

}
