package io.runon.stock.trading.market;

import io.runon.trading.CountryCode;
import io.runon.trading.Time;
import io.runon.trading.TradingGson;
import io.runon.trading.TradingTimes;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 증시자금 일별
 * @author macle
 */
@Data
public class MarketFundDaily implements Time {

    public static final MarketFundDaily[] EMPTY_ARRAY = new MarketFundDaily[0];
    public static final Comparator<MarketFundDaily> SORT = Comparator.comparingInt(o -> o.ymd);

    Long t ;

    int ymd;

    //투자자 예탁금
    BigDecimal deposit;

    //장내파생상품 거래 예수금
    BigDecimal derivativesDeposit;

    //대고객환매 조건부채권(RP) 매도잔고
    BigDecimal rpSellBalance;

    //위탁매매 미수금
    BigDecimal accountsReceivable;

    //위탁매매 미수금 대비 실제 반대매매 금액
    BigDecimal liquidation  ;

    //미수금대비 반대매매 비중
    BigDecimal liquidationPercent;

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

    public static MarketFundDaily make(String jsonStr, CountryCode countryCode){
        MarketFundDaily daily = TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, MarketFundDaily.class);
        if(daily.t == null){
            daily.t = TradingTimes.getDailyOpenTime(countryCode, Integer.toString(daily.ymd));
        }

        return daily;
    }

    public String outTimeLineJsonText(CountryCode countryCode){
        if(t == null){
            t = TradingTimes.getDailyOpenTime(countryCode, Integer.toString(ymd));
        }

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }
}
