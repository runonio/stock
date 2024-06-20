package io.runon.stock.trading;

import io.runon.stock.trading.data.management.StockOutTimeLineJson;
import io.runon.trading.TradingGson;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 매매동향 일별 정보
 * @author macle
 */
@Data
public class StockInvestorDaily implements StockOutTimeLineJson {

    public static final StockInvestorDaily [] EMPTY_ARRAY = new StockInvestorDaily[0];
    public static final Comparator<StockInvestorDaily> SORT = Comparator.comparingInt(o -> o.ymd);

    Long t ;

    int ymd;

    BigDecimal close;
    BigDecimal change;

    //사모펀드
    BigDecimal privateFund;
    BigDecimal privateFundPrice;
    //증권
    BigDecimal securities;
    BigDecimal securitiesPrice;

//    https://openapi.ls-sec.co.kr/apiservice?group_id=73142d9f-1983-48d2-8543-89b75535d34c&api_id=90378c39-f93e-4f95-9670-f76e5c924cc6
    //보험
    BigDecimal insurance;
    BigDecimal insurancePrice;

    //투자신탁(투신)
    BigDecimal investmentTrust;
    BigDecimal investmentTrustPrice;

    //은행
    BigDecimal bank;
    BigDecimal bankPrice;

    //종합금융회사(종금)
    BigDecimal generalFinance;
    BigDecimal generalFinancePrice;

    //기금(연기금)
    BigDecimal pensionFund;
    BigDecimal pensionFundPrice;
    //기타법인
    BigDecimal otherCorporation;
    BigDecimal otherCorporationPrice;

    //개인
    BigDecimal individual;
    BigDecimal individualPrice;


    //등록외국인
    BigDecimal foreignerReg;
    BigDecimal foreignerRegPrice;


    //미등록외국인
    BigDecimal foreignerUnReg;
    BigDecimal foreignerUnRegPrice;

    //국가외(국가내의 기타를 뜻하는것 같음, 정확한 용어정리가 되면 변경될 여지가 있음)
    BigDecimal countryEtc;
    BigDecimal countryEtcPrice;

    //기관
    BigDecimal institution;
    BigDecimal institutionPrice;

    //dhl국계
    BigDecimal foreign;
    BigDecimal foreignPrice;

    //기타계 (기타 + 국가)
    BigDecimal etc;
    BigDecimal etcPrice;


    public static StockInvestorDaily make(String jsonStr, Stock stock){

        StockInvestorDaily daily = make(jsonStr);
        if(daily.t == null){
            daily.t = Stocks.getDailyOpenTime(stock, daily.ymd);
        }

        return daily;
    }

    public static StockInvestorDaily make(String jsonStr){

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, StockInvestorDaily.class);
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