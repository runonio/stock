package io.runon.stock.trading;

import io.runon.trading.CountryCode;
import io.runon.trading.TradingGson;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 증시자금 일별
 * @author macle
 */
@Data
public class StockMarketFundDaily {

    public static final StockMarketFundDaily [] EMPTY_ARRAY = new StockMarketFundDaily[0];
    public static final Comparator<StockMarketFundDaily> SORT = Comparator.comparingInt(o -> o.ymd);

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


    public static StockMarketFundDaily make(String jsonStr, CountryCode countryCode){
        StockMarketFundDaily daily = TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, StockMarketFundDaily.class);
        if(daily.t == null){
            daily.t = Stocks.getDailyOpenTime(countryCode, Integer.toString(daily.ymd));
        }

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, StockMarketFundDaily.class);
    }



    public String outTimeLineJsonText(Stock stock){
        if(t == null){
            t = Stocks.getDailyOpenTime(stock, ymd);
        }

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }
}
