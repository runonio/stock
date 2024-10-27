package io.runon.stock.trading;

import io.runon.trading.TradingGson;
import io.runon.trading.data.RatingScore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 자사주 매매
 * @author macle
 */
@Data
public class TreasuryStockTrading {
    //직접 신탁
    String methodType;
    //취득 처분 유형 (BUY, SELL)
    String tradeType;
    
    //시작 년월일
    Integer beginYmd;
    //종료 년월일
    Integer endYmd;
    
    //신고 수량
    BigDecimal regQuantity;
    //신고 금액
    BigDecimal regTradingPrice;

    //매매수량
    BigDecimal tradingQuantity;
    //매매비율
    BigDecimal tradingRate;
    //매매금액
    BigDecimal tradingPrice;

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    public static TreasuryStockTrading make(String jsonText){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonText, TreasuryStockTrading.class);
    }
}
