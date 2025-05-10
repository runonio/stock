package io.runon.stock.trading;

import io.runon.commons.utils.GsonUtils;
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
    BigDecimal regVolume;
    //신고 금액
    BigDecimal regAmount;

    //매매수량
    BigDecimal volume;
    //매매금액
    BigDecimal amount;
    //매매비율
    BigDecimal tradingRate;


    @Override
    public String toString(){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    public static TreasuryStockTrading make(String jsonText){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonText, TreasuryStockTrading.class);
    }
}
