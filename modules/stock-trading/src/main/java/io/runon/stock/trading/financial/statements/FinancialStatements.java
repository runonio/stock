package io.runon.stock.trading.financial.statements;

import io.runon.commons.utils.GsonUtils;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 재무제표
 * @author macle
 */
@Data
public class FinancialStatements {

    //결산년열
    String ym;
    //매출액
    BigDecimal sales;
    //영업이익
    BigDecimal operatingProfit;

    //순이익
    BigDecimal netProfit;

    //자산
    BigDecimal assets;
    //부채
    BigDecimal liabilities;
    //자본 총계
    BigDecimal capital;

    //영업활동현금흐름
    BigDecimal operatingCashFlow;

    //현금성자산
    BigDecimal cashEquivalents;

    public String getYear(){
        return ym.substring(0, 4);
    }

    public String getMonth(){
        return ym.substring(4, 6);
    }


    @Override
    public String toString(){
        return  GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
