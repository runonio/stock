package io.runon.stock.trading;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.seomse.commons.utils.time.YmdUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Comparator;

/**
 * 주식 일별 대차 (대주)
 * 액면병합과 액면분할등으로 정보가 달라질 수 주식수와 주가정보 거래량정보등이 같이 포함 됨
 * 간련정보는 1거래일 전일것까지 제공된다. 1월27일 거래에는 1월26일것까지 확인할 수 있음
 * @author macle
 */
@Data
public class StockLoanDaily {

    public static final StockLoanDaily [] EMPTY_ARRAY = new StockLoanDaily[0];
    public static final Comparator<StockLoanDaily> SORT = Comparator.comparingInt(o -> o.ymd);

    Long t ;

    int ymd;

    //발행주식수 또는 유통주식수
    //국내랑 해외랑 다를 수 있는 부분
    BigDecimal shares;

    //거래량
    BigDecimal volume;

    //종가
    BigDecimal close;

    //대차(대추) 체결량
    BigDecimal loanTransaction;

    //대차(대주) 상환량
    BigDecimal loanRepayment ;

    //대차(대추) 잔고
    BigDecimal loanBalance;
    @Override
    public String toString(){

        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }

    public static StockLoanDaily make(String jsonStr, Stock stock){

        StockLoanDaily stockLoanDaily = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create().fromJson(jsonStr, StockLoanDaily.class);
        if(stockLoanDaily.t == null){
            stockLoanDaily.t = Stocks.getDailyOpenTime(stock, stockLoanDaily.ymd);
        }

        return stockLoanDaily;
    }


    public long getTime(){
        return t;
    }

    public void setTime(long time){
        this.t = time;
    }

}
