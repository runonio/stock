package io.runon.stock.trading.data;

import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.daily.*;
import io.runon.trading.data.TimeNumbersMap;
import io.runon.trading.data.daily.VolumePowerDaily;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 분석에 필요한 정보를 메모리에 올려놓기 위한 정보
 * @author macle
 */
@Data
public class StockDataStore {

    private Stock stock;

    private final Map<String, Object> dataMap = new HashMap<>();

    TradeCandle [] candles = null; //캔들
    StockInvestorDaily[] investorDailies = null; //투자자별
    ShortSellingDaily[] shortSellingDailies = null; //공매도
    ProgramDaily[] programDailies = null; //프로그램
    StockLoanDaily[] stockLoanDailies = null;//대차잔고

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

    public StockDataStore(Stock stock){
        this.stock = stock;
    }


    public void setData(String beginYmd, String endYmd){
        setData(Integer.parseInt(beginYmd), Integer.parseInt(endYmd));
    }

    public void setData(int beginYmd, int endYmd){

        candles = StockDataLoad.getCandle(candles, stock, beginYmd, endYmd);

        investorDailies = StockDataLoad.getInvestor(investorDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, investorDayGap));
        shortSellingDailies = StockDataLoad.getShortSelling(shortSellingDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, shortSellingDayGap));
        programDailies = StockDataLoad.getProgram(programDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, programDayGap));
        stockLoanDailies = StockDataLoad.getStockLoan(stockLoanDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, stockLoanDayGap));

        stockCreditLoanDailies = StockDataLoad.getStockCreditLoan(stockCreditLoanDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, stockCreditLoanDayGap));
        volumePowerDailies = StockDataLoad.getVolumePower(volumePowerDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, volumePowerDayGap));
    }

    public void setDayGap(StockDataStoreParam dailyStoreGap){

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


    public void putData(String key, Object value) {
        dataMap.put(key, value);
    }

    public TimeNumbersMap[] getTimeNumbersMaps(String key) {
        Object obj = dataMap.get(key);
        if(obj == null){
            return null;
        }
        return (TimeNumbersMap []) obj;
    }

    public TimeNumbersMap getTimeNumbersMap(String key, int ymd){

        TimeNumbersMap [] maps = getTimeNumbersMaps(key);
        if(maps == null){
            return null;
        }

        for(TimeNumbersMap timeNumbersMap : maps){
            if(timeNumbersMap.getYmd() == ymd){
                return timeNumbersMap;
            }
        }

        return null;
    }

    public TimeNumbersMap getTimeNumbersMap(String key, long time){

        TimeNumbersMap [] maps = getTimeNumbersMaps(key);
        if(maps == null){
            return null;
        }

        for(TimeNumbersMap timeNumbersMap : maps){
            if(timeNumbersMap.getTime() == time){
                return timeNumbersMap;
            }
        }

        return null;
    }


    public static Stock [] getStocks(StockDataStore [] array){
        Stock [] stocks = new Stock[array.length];
        for (int i = 0; i <stocks.length ; i++) {
            stocks[i] = array[i].stock;
        }

        return stocks;
    }
}