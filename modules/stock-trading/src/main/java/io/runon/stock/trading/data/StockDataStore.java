package io.runon.stock.trading.data;

import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.daily.*;
import io.runon.trading.TimeNumber;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.TimeNumbersMap;
import io.runon.trading.data.daily.VolumePowerDaily;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.TradeCandleIndex;
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

    boolean isInvestor = true;

    boolean isShortSelling = true;

    boolean isProgram = true;

    boolean isStockLoan = true;

    boolean isCreditLoan = true;

    boolean isVolumePower = true;

    public StockDataStore(Stock stock){
        this.stock = stock;
    }


    public void setData(String beginYmd, String endYmd){
        setData(Integer.parseInt(beginYmd), Integer.parseInt(endYmd));
    }

    public void setData(int beginYmd, int endYmd){

        candles = StockDataLoad.getCandle(candles, stock, beginYmd, endYmd);

        if(isInvestor) {
            investorDailies = StockDataLoad.getInvestor(investorDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, investorDayGap));
        }
        if(isShortSelling) {
            shortSellingDailies = StockDataLoad.getShortSelling(shortSellingDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, shortSellingDayGap));
        }
        if(isProgram) {
            programDailies = StockDataLoad.getProgram(programDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, programDayGap));
        }
        if(isStockLoan) {
            stockLoanDailies = StockDataLoad.getStockLoan(stockLoanDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, stockLoanDayGap));
        }
        if(isCreditLoan) {
            stockCreditLoanDailies = StockDataLoad.getStockCreditLoan(stockCreditLoanDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, stockCreditLoanDayGap));
        }
        if(isVolumePower) {
            volumePowerDailies = StockDataLoad.getVolumePower(volumePowerDailies, stock, beginYmd, YmdUtil.getYmdInt(endYmd, volumePowerDayGap));
        }
    }

    public void set(StockDataStoreParam param){

        if(param == null){
            return;
        }

        investorDayGap = param.investorDayGap;
        shortSellingDayGap = param.shortSellingDayGap;
        programDayGap = param.programDayGap;
        stockLoanDayGap = param.stockLoanDayGap;
        stockCreditLoanDayGap = param.stockCreditLoanDayGap;
        volumePowerDayGap = param.volumePowerDayGap;

        isInvestor = param.isInvestor;
        isShortSelling = param.isShortSelling;
        isProgram = param.isProgram;
        isStockLoan = param.isStockLoan;
        isCreditLoan = param.isCreditLoan;
        isVolumePower = param.isVolumePower;
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

    public TimeNumbersMap getTimeNumbersMap(String key, long beginTime, long endTime){

        TimeNumbersMap [] maps = getTimeNumbersMaps(key);
        if(maps == null){
            return null;
        }

        for(TimeNumbersMap timeNumbersMap : maps){
            if(timeNumbersMap.getTime() >= beginTime && timeNumbersMap.getTime() < endTime){
                return timeNumbersMap;
            }
        }

        return null;
    }





    public TradeCandleIndex getCandle( long beginTime, long endTime, int lastIndex){


        for (int i = lastIndex; i <candles.length ; i++) {
            TradeCandle candle = candles[i];
            if(beginTime <= candle.getOpenTime() && endTime > candle.getOpenTime()){

                //검색성공
                return new TradeCandleIndex(candle, i);
            }

        }

        for (int i = lastIndex-1; i > -1; i--) {
            TradeCandle candle = candles[i];
            if(beginTime <= candle.getOpenTime() && endTime > candle.getOpenTime()){
                //검색성공
                return new TradeCandleIndex(candle, i);
            }

        }


//        System.out.println("null: " + stock.getStockId() +", "  +  YmdUtil.getYmd(beginTime, TradingTimes.KOR_ZONE_ID));

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