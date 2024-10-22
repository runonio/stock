package io.runon.stock.trading.daily;

import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.candle.StockCandles;
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

    TradeCandle [] candles = null; //캔들
    StockInvestorDaily [] investorDailies = null; //투자자별
    ShortSellingDaily [] shortSellingDailies = null; //공매도
    ProgramDaily [] programDailies = null; //프로그램
    StockLoanDaily [] stockLoanDailies = null;//대차잔고

    StockCreditLoanDaily[] stockCreditLoanDailies = null; //종목별 신용정보
    VolumePowerDaily [] volumePowerDailies = null; //체결강도

    //투자자별 매매동향
    int investorDayGap = -1;

    //공매도
    int shortSellingDayGap = -2;

    //프로그램
    int programDayGap = 0;

    //대차잔고
    int stockLoanDayGap = -1;

    //종목별 신용
    int stockCreditLoanDayGap = -1;

    //체결강도
    int volumePowerDayGap = 0;

    public StockDailyStore(Stock stock){
        this.stock = stock;
    }


    public void setData(String beginYmd, String endYmd){
        setData(Integer.parseInt(beginYmd), Integer.parseInt(endYmd));
    }

    public void setData(int beginYmd, int endYmd){

        candles = StockDailies.getCandle(candles, stock, beginYmd, endYmd);

        investorDailies = StockDailies.getInvestor(investorDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, investorDayGap));
        shortSellingDailies = StockDailies.getShortSelling(shortSellingDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, shortSellingDayGap));
        programDailies = StockDailies.getProgram(programDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, programDayGap));
        stockLoanDailies = StockDailies.getStockLoan(stockLoanDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, stockLoanDayGap));

        stockCreditLoanDailies = StockDailies.getStockCreditLoan(stockCreditLoanDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, stockCreditLoanDayGap));
        volumePowerDailies = StockDailies.getVolumePower(volumePowerDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, volumePowerDayGap));
    }

    public void setDayGap(StockDailyStoreGap dailyStoreGap){

        if(dailyStoreGap == null){
            return;
        }

        investorDayGap = dailyStoreGap.investorDayGap;
        shortSellingDayGap = dailyStoreGap.shortSellingDayGap;
        programDayGap = dailyStoreGap.programDayGap;
        stockLoanDayGap = dailyStoreGap.stockLoanDayGap;
        stockCreditLoanDayGap = dailyStoreGap.stockCreditLoanDayGap;
        volumePowerDayGap = dailyStoreGap.volumePowerDayGap;
    }
}