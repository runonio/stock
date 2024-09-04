package io.runon.stock.trading.market;

import io.runon.trading.CountryCode;
import io.runon.trading.TradingGson;
import io.runon.trading.TradingTimes;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 한국증시 신용
 * @author macle
 */
@Data
public class KorMarketCreditLoanDaily {

    public static final KorMarketCreditLoanDaily[] EMPTY_ARRAY = new KorMarketCreditLoanDaily[0];
    public static final Comparator<KorMarketCreditLoanDaily> SORT = Comparator.comparingInt(o -> o.ymd);


    Long t ;

    int ymd;


    BigDecimal kospi;

    BigDecimal kosdaq;

    public BigDecimal getLoan(){
        return kospi.add(kosdaq);
    }

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }


    public static KorMarketCreditLoanDaily make(String jsonStr, CountryCode countryCode){
        KorMarketCreditLoanDaily daily = TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, KorMarketCreditLoanDaily.class);
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
