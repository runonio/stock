package io.runon.stock.trading.daily;

import io.runon.stock.trading.Stock;
import io.runon.trading.data.daily.VolumePowerDaily;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.Data;

/**
 * 주식 일별 분석 정보
 * @author macle
 */
@Data
public class StockDailyStore {


    private Stock stock;

    private int arrayCount = 1000;

    TradeCandle [] candles; //캔들
    StockInvestorDaily [] investorDailies; //투자자별
    ShortSellingDaily [] shortSellingDailies; //공매도
    ProgramDaily [] programDailies; //프로그램
    StockLoanDaily [] stockLoanDailies;//대차잔고

    StockCreditLoanDaily[] stockCreditLoanDailies; //종목별 신용정보
    VolumePowerDaily [] volumePowerDailies; //체결강도


    public StockDailyStore(Stock stock){
        this.stock = stock;
    }

    private int standardYmd;


    public void setData(String standardYmd){
        setData(Integer.parseInt(standardYmd));
    }


    public void setData(int standardYmd){

    }

}
