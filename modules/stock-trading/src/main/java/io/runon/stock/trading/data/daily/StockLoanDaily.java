package io.runon.stock.trading.data.daily;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.management.StockOutTimeLineJson;
import io.runon.trading.Time;
import io.runon.trading.TimeNumber;
import io.runon.trading.TradingGson;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Comparator;

/**
 * 주식 일별 대차 (대주)
 * 액면병합과 액면분할등으로 정보가 달라질 수 주식수와 주가정보 거래량정보등이 같이 포함 됨
 * 간련정보는 1거래일 전일것까지 제공된다. 1월27일 거래에는 1월26일것까지 확인할 수 있음
 * @author macle
 */
@Data
public class StockLoanDaily implements StockOutTimeLineJson, TimeNumber {

    public static final StockLoanDaily [] EMPTY_ARRAY = new StockLoanDaily[0];
    public static final Comparator<StockLoanDaily> SORT = Comparator.comparingInt(o -> o.ymd);

    int scale = 0;

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
    public static StockLoanDaily make(String jsonStr, Stock stock){

        StockLoanDaily stockLoanDaily = make(jsonStr);
        if(stockLoanDaily.t == null){
            stockLoanDaily.t = Stocks.getDailyOpenTime(stock, stockLoanDaily.ymd);
        }

        return stockLoanDaily;
    }

    public static StockLoanDaily make(String jsonStr){

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, StockLoanDaily.class);
    }

    public void initZero(){
        if(shares == null){
            shares = BigDecimal.ZERO;
        }

        if(volume == null){
            volume = BigDecimal.ZERO;
        }

        if(loanTransaction == null) {
            loanTransaction = BigDecimal.ZERO;
        }

        if(loanRepayment == null) {
            loanRepayment = BigDecimal.ZERO;
        }

        if(loanBalance == null){
            loanBalance = BigDecimal.ZERO;
        }
    }

    public void init(TradeCandle candle){

        if(close == null){
            return;
        }

        if(close.compareTo(candle.getClose()) == 0){
            return;
        }

        initZero();

        BigDecimal changeRate = close.divide(candle.getClose(), MathContext.DECIMAL128);

        this.close = candle.getClose();

        shares =shares.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        volume = volume.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);

        loanTransaction = loanTransaction.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        loanRepayment = loanRepayment.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
        loanBalance = loanBalance.multiply(changeRate).setScale(scale, RoundingMode.HALF_UP);
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

    @Override
    public long getTime(){
        return t;
    }

    public void setTime(long time){
        this.t = time;
    }

    @Override
    public BigDecimal getNumber() {
        return loanBalance;
    }
}
