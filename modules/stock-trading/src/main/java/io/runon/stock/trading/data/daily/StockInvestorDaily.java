package io.runon.stock.trading.data.daily;

import io.runon.commons.utils.GsonUtils;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.management.StockOutTimeLineJson;
import io.runon.trading.TimeNumber;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Comparator;

/**
 * 투신과 사모펀트는 장기적 상승이 있을만한 종목에 투자한다
 * 연기금과 기타법인(자사주)과 같은성격은 단기적 상승, 혹은 가격방어적 성향을 띈다.
 * 외국인은 국제적 영향을 같이 생각해야한다.우리나라 수출, 국가경쟁력이 강해지고 투자시장이 선진화되면 외국인 자산이 들어올 수 잇고, 그 반대면 빠져나간다.
 * 외국인 투자는 환율과 직접적인 관계까 크다.
 * 매매동향 일별 정보
 * @author macle
 */
@Data
public class StockInvestorDaily implements StockOutTimeLineJson, TimeNumber {

    public static final StockInvestorDaily [] EMPTY_ARRAY = new StockInvestorDaily[0];
    public static final Comparator<StockInvestorDaily> SORT = Comparator.comparingInt(o -> o.ymd);

    int scale = 0;

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


    public void initZero(){

        if(privateFund == null){
            privateFund = BigDecimal.ZERO;
        }
        if(privateFundPrice == null){
            privateFundPrice = BigDecimal.ZERO;
        }

        if(securities == null){
            securities = BigDecimal.ZERO;
        }
        if(securitiesPrice == null){
            securitiesPrice = BigDecimal.ZERO;
        }

        if(insurance == null){
            insurance = BigDecimal.ZERO;
        }
        if(insurancePrice == null){
            insurancePrice = BigDecimal.ZERO;
        }

        if(investmentTrust == null){
            investmentTrust = BigDecimal.ZERO;
        }
        if(investmentTrustPrice == null){
            investmentTrustPrice = BigDecimal.ZERO;
        }
        if(bank == null){
            bank = BigDecimal.ZERO;
        }
        if(bankPrice == null){
            bankPrice = BigDecimal.ZERO;
        }

        if(generalFinance == null){
            generalFinance = BigDecimal.ZERO;
        }
        if(generalFinancePrice == null){
            generalFinancePrice = BigDecimal.ZERO;
        }

        if(pensionFund == null){
            pensionFund = BigDecimal.ZERO;
        }
        if(pensionFundPrice == null){
            pensionFundPrice = BigDecimal.ZERO;
        }

        if(otherCorporation == null){
            otherCorporation = BigDecimal.ZERO;
        }
        if(otherCorporationPrice == null){
            otherCorporationPrice = BigDecimal.ZERO;
        }

        if(individual == null){
            individual = BigDecimal.ZERO;
        }
        if(individualPrice == null){
            individualPrice = BigDecimal.ZERO;
        }
        if(foreignerReg == null){
            foreignerReg = BigDecimal.ZERO;
        }
        if(foreignerRegPrice == null){
            foreignerRegPrice = BigDecimal.ZERO;
        }

        if(foreignerUnReg == null){
            foreignerUnReg = BigDecimal.ZERO;
        }
        if(foreignerUnRegPrice == null){
            foreignerUnRegPrice = BigDecimal.ZERO;
        }

        if(countryEtc == null){
            countryEtc = BigDecimal.ZERO;
        }
        if(countryEtcPrice == null){
            countryEtcPrice = BigDecimal.ZERO;
        }
        
        if(institution == null){
            institution = BigDecimal.ZERO;
        }
        if(institutionPrice == null){
            institutionPrice = BigDecimal.ZERO;
        }
        
        if(foreign == null){
            foreign = BigDecimal.ZERO;
        }
        if(foreignPrice == null){
            foreignPrice = BigDecimal.ZERO;
        }

        if(etc == null){
            etc = BigDecimal.ZERO;
        }
        if(etcPrice == null){
            etcPrice = BigDecimal.ZERO;
        }
    }

    public void init(TradeCandle candle){


        //종가를 비교해서 분할 합병에 대해 수량을 맞춤
        if(close.compareTo(candle.getClose()) == 0){
            return;
        }

        initZero();

        BigDecimal changeRate = close.divide(candle.getClose(), MathContext.DECIMAL128);

        this.close = candle.getClose();

        privateFund =privateFund.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        privateFundPrice = privateFundPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        securities = securities.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        securitiesPrice = securitiesPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        insurance = insurance.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        insurancePrice = insurancePrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        investmentTrust = investmentTrust.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        investmentTrustPrice = investmentTrustPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        bank = bank.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        bankPrice = bankPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        generalFinance = generalFinance.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        generalFinancePrice = generalFinancePrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        pensionFund = pensionFund.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        pensionFundPrice = pensionFundPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        otherCorporation = otherCorporation.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        otherCorporationPrice = otherCorporationPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        individual = individual.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        individualPrice = individualPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        foreignerReg = foreignerReg.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        foreignerRegPrice = foreignerRegPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        foreignerUnReg = foreignerUnReg.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        foreignerUnRegPrice = foreignerUnRegPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        countryEtc = countryEtc.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        countryEtcPrice = countryEtcPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        institution = institution.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        institutionPrice = institutionPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        foreign = foreign.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        foreignPrice = foreignPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        etc = etc.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        etcPrice = etcPrice.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
    }

    public static StockInvestorDaily make(String jsonStr, Stock stock){

        StockInvestorDaily daily = make(jsonStr);
        if(daily.t == null){
            daily.t = Stocks.getDailyOpenTime(stock, daily.ymd);
        }

        return daily;
    }

    public static StockInvestorDaily make(String jsonStr){

        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, StockInvestorDaily.class);
    }


    @Override
    public String outTimeLineJsonText(Stock stock){
        if(t == null){
            t = Stocks.getDailyOpenTime(stock, ymd);
        }

        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    @Override
    public String toString(){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }

    @Override
    public long getTime(){
        return t;
    }

    public void setTime(long time){
        this.t = time;
    }

    //투신과 사모
    @Override
    public BigDecimal getNumber() {
//          투신, 사모,
        return investmentTrust.add(privateFund);
    }

}