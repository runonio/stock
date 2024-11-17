package io.runon.stock.trading.daily;

import com.seomse.commons.callback.StrCallback;
import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.candle.StockCandles;
import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.data.daily.VolumePowerDaily;
import io.runon.trading.data.file.TimeLine;
import io.runon.trading.data.file.TimeLines;
import io.runon.trading.data.file.TimeName;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author macle
 */
public class StockDailies {
    /**
     * @return 캔들
     */
    public static TradeCandle [] getCandle(Stock stock, int beginYmd, int endYmd){
        return StockCandles.getDailyCandles(stock, beginYmd, endYmd);
    }

    public static TradeCandle [] getCandle( TradeCandle [] lastCandles, Stock stock, int beginYmd, int endYmd){

        //라스트 1개는 다시 불러 온다. 값이 변경될 수 있다.
        if(lastCandles == null || lastCandles.length < 2){
            return getCandle(stock, beginYmd, endYmd);
        }

        ZoneId zoneId = Stocks.getZoneId(stock);

        List<TradeCandle> list = new ArrayList<>();
        int end = lastCandles.length-1;
        for (int i = 0; i < end ; i++) {
            TradeCandle tradeCandle = lastCandles[i];
            int ymd =  YmdUtil.getYmdInt(tradeCandle.getOpenTime(), zoneId);
            if(ymd < beginYmd){
                continue;
            }
            if(ymd > endYmd){
                continue;
            }
            list.add(tradeCandle);
        }

        //라스트 1개부터 다시 시작하기때문에 마지막 날짜로
        int newBeginYmd = YmdUtil.getYmdInt(lastCandles[lastCandles.length - 1 ].getOpenTime(), zoneId);
        if(newBeginYmd < beginYmd){
            newBeginYmd = beginYmd;
        }

        if(newBeginYmd >= endYmd){

            return list.toArray(new TradeCandle[0]);
        }

        TradeCandle [] candles = StockCandles.getDailyCandles(stock, newBeginYmd, endYmd);
        list.addAll(Arrays.asList(candles));

        return list.toArray(new TradeCandle[0]);
    }

    /**
     * @return 투자자별 매매동향
     */
    public static StockInvestorDaily [] getInvestor(Stock stock, int beginYmd, int endYmd){
        String filesPath = StockPaths.getInvestorFilesPath(stock.getStockId(), "1d");

        ZoneId zoneId = Stocks.getZoneId(stock);

        List<StockInvestorDaily> list = new ArrayList<>();
        StrCallback callback = str -> list.add(StockInvestorDaily.make(str, stock));
        // 0시 0분 0초를 주므로 +1일을 해준다.
        TimeLines.load(filesPath,  YmdUtil.getTime(beginYmd, zoneId), YmdUtil.getTime(endYmd,zoneId) + (Times.DAY_1 - 1L) ,TimeName.getDefaultType(Times.DAY_1), TimeLine.JSON,callback);
        return list.toArray(new StockInvestorDaily[0]);
    }

    public static StockInvestorDaily [] getInvestor(StockInvestorDaily [] lastDailies, Stock stock, int beginYmd, int endYmd){
        if(lastDailies == null || lastDailies.length < 2){
            return getInvestor(stock, beginYmd, endYmd);
        }

        //라스트 1개는 다시 불러 온다. 값이 변경될 수 있다.
        List<StockInvestorDaily> list = new ArrayList<>();

        int end = lastDailies.length-1;

        for (int i = 0; i < end; i++) {
            StockInvestorDaily daily = lastDailies[i];

            int ymd = daily.getYmd();
            if(ymd < beginYmd){
                continue;
            }
            if(ymd > endYmd){
                continue;
            }
            list.add(daily);
        }


        int newBeginYmd = lastDailies[lastDailies.length - 1 ].getYmd();

        if(newBeginYmd < beginYmd){
            newBeginYmd = beginYmd;
        }

        if(newBeginYmd >= endYmd){
            return list.toArray(new StockInvestorDaily[0]);
        }

        StockInvestorDaily [] dailies = getInvestor(stock, newBeginYmd, endYmd);
        Collections.addAll(list, dailies);

        return list.toArray(new StockInvestorDaily[0]);
    }

    /**
     * @return 공매도
     */
    public static ShortSellingDaily [] getShortSelling(Stock stock, int beginYmd, int endYmd){

        ZoneId zoneId = Stocks.getZoneId(stock);

        String filesPath = StockPaths.getShortSellingFilesPath(stock.getStockId(), "1d");

        List<ShortSellingDaily> list = new ArrayList<>();
        StrCallback callback = str -> list.add(ShortSellingDaily.make(str, stock));
        TimeLines.load(filesPath,  YmdUtil.getTime(beginYmd, zoneId), YmdUtil.getTime(endYmd,zoneId) + (Times.DAY_1 - 1L) ,TimeName.getDefaultType(Times.DAY_1), TimeLine.JSON,callback);

        return list.toArray(new ShortSellingDaily[0]);
    }

    public static ShortSellingDaily [] getShortSelling(ShortSellingDaily [] lastDailies, Stock stock, int beginYmd, int endYmd){
        if(lastDailies == null || lastDailies.length == 0){
            return getShortSelling(stock, beginYmd, endYmd);
        }

        //라스트 1개는 다시 불러 온다. 값이 변경될 수 있다.
        List<ShortSellingDaily> list = new ArrayList<>();
        int end = lastDailies.length-1;

        for (int i = 0; i <end ; i++) {
            ShortSellingDaily daily = lastDailies[i];
            int ymd = daily.getYmd();
            if(ymd < beginYmd){
                continue;
            }
            if(ymd > endYmd){
                continue;
            }
            list.add(daily);
        }


        int newBeginYmd = lastDailies[lastDailies.length - 1].getYmd();

        if(newBeginYmd < beginYmd){
            newBeginYmd = beginYmd;
        }

        if(newBeginYmd >= endYmd){
            return list.toArray(new ShortSellingDaily[0]);
        }

        ShortSellingDaily [] dailies = getShortSelling(stock, newBeginYmd, endYmd);
        Collections.addAll(list, dailies);

        return list.toArray(new ShortSellingDaily[0]);
    }


    /**
     * @return 프로그램 매매동향
     */
    public static ProgramDaily [] getProgram(Stock stock, int beginYmd, int endYmd){

        ZoneId zoneId = Stocks.getZoneId(stock);
        String filesPath = StockPaths.getProgramFilesPath(stock.getStockId(), "1d");

        List<ProgramDaily> list = new ArrayList<>();

        StrCallback callback = str -> list.add(ProgramDaily.make(str, stock));
        TimeLines.load(filesPath,  YmdUtil.getTime(beginYmd, zoneId), YmdUtil.getTime(endYmd,zoneId) + (Times.DAY_1 - 1L),TimeName.getDefaultType(Times.DAY_1), TimeLine.JSON,callback);
        return list.toArray(new ProgramDaily[0]);
    }

    public static ProgramDaily [] getProgram(ProgramDaily [] lastDailies, Stock stock, int beginYmd, int endYmd) {
        if (lastDailies == null || lastDailies.length == 0) {
            return getProgram(stock, beginYmd, endYmd);
        }

        List<ProgramDaily> list = new ArrayList<>();
        int end = lastDailies.length - 1;

        for (int i = 0; i < end; i++) {
            ProgramDaily daily = lastDailies[i];
            int ymd = daily.getYmd();
            if (ymd < beginYmd) {
                continue;
            }
            if (ymd > endYmd) {
                continue;
            }
            list.add(daily);
        }

        int newBeginYmd = lastDailies[lastDailies.length - 1].getYmd();

        if (newBeginYmd < beginYmd) {
            newBeginYmd = beginYmd;
        }

        if (newBeginYmd >= endYmd) {
            return list.toArray(new ProgramDaily[0]);
        }

        ProgramDaily[] dailies = getProgram(stock, newBeginYmd, endYmd);
        Collections.addAll(list, dailies);

        return list.toArray(new ProgramDaily[0]);
    }

    /**
     * @return 대차잔고
     */
    public static StockLoanDaily [] getStockLoan(Stock stock, int beginYmd, int endYmd){

        ZoneId zoneId = Stocks.getZoneId(stock);
        String filesPath = StockPaths.getStockLoanFilesPath(stock.getStockId(), "1d");

        List<StockLoanDaily> list = new ArrayList<>();
        StrCallback callback = str -> list.add(StockLoanDaily.make(str, stock));

        TimeLines.load(filesPath,  YmdUtil.getTime(beginYmd, zoneId), YmdUtil.getTime(endYmd,zoneId) + (Times.DAY_1 - 1L),TimeName.getDefaultType(Times.DAY_1), TimeLine.JSON,callback);
        return list.toArray(new StockLoanDaily[0]);
    }

    public static StockLoanDaily [] getStockLoan( StockLoanDaily[] lastDailies, Stock stock, int beginYmd, int endYmd){

        if (lastDailies == null || lastDailies.length == 0) {
            return getStockLoan(stock, beginYmd, endYmd);
        }

        List<StockLoanDaily> list = new ArrayList<>();
        int end = lastDailies.length - 1;

        for (int i = 0; i < end; i++) {
            StockLoanDaily daily = lastDailies[i];
            int ymd = daily.getYmd();
            if (ymd < beginYmd) {
                continue;
            }
            if (ymd > endYmd) {
                continue;
            }
            list.add(daily);
        }

        int newBeginYmd = lastDailies[lastDailies.length - 1].getYmd();

        if (newBeginYmd < beginYmd) {
            newBeginYmd = beginYmd;
        }

        if (newBeginYmd >= endYmd) {
            return list.toArray(new StockLoanDaily[0]);
        }

        StockLoanDaily[] dailies = getStockLoan(stock, newBeginYmd, endYmd);
        Collections.addAll(list, dailies);

        return list.toArray(new StockLoanDaily[0]);
    }

    /**
     * @return 종목별신용정보
     */
    public static StockCreditLoanDaily [] getStockCreditLoan(Stock stock, int beginYmd, int endYmd){

        ZoneId zoneId = Stocks.getZoneId(stock);
        String filesPath = StockPaths.getSpotCreditLoanFilesPath(stock.getStockId(), "1d");

        List<StockCreditLoanDaily> list = new ArrayList<>();
        StrCallback callback = str -> list.add(StockCreditLoanDaily.make(str));

        TimeLines.load(filesPath,  YmdUtil.getTime(beginYmd, zoneId), YmdUtil.getTime(endYmd,zoneId) + (Times.DAY_1 - 1L),TimeName.getDefaultType(Times.DAY_1), TimeLine.JSON,callback);
        return list.toArray(new StockCreditLoanDaily[0]);
    }
    public static StockCreditLoanDaily [] getStockCreditLoan(StockCreditLoanDaily [] lastDailies,Stock stock, int beginYmd, int endYmd){
        if (lastDailies == null || lastDailies.length == 0) {
            return getStockCreditLoan(stock, beginYmd, endYmd);
        }

        List<StockCreditLoanDaily> list = new ArrayList<>();
        int end = lastDailies.length - 1;

        for (int i = 0; i < end; i++) {
            StockCreditLoanDaily daily = lastDailies[i];
            int ymd = daily.getTradeYmd();
            if (ymd < beginYmd) {
                continue;
            }
            if (ymd > endYmd) {
                continue;
            }
            list.add(daily);
        }

        int newBeginYmd = lastDailies[lastDailies.length - 1].getTradeYmd();

        if (newBeginYmd < beginYmd) {
            newBeginYmd = beginYmd;
        }

        if (newBeginYmd >= endYmd) {
            return list.toArray(new StockCreditLoanDaily[0]);
        }

        StockCreditLoanDaily[] dailies = getStockCreditLoan(stock, newBeginYmd, endYmd);
        Collections.addAll(list, dailies);

        return list.toArray(new StockCreditLoanDaily[0]);

    }

    /**
     * 종목별 체결강도
     * @return 종목별신용정보
     */
    public static VolumePowerDaily [] getVolumePower(Stock stock, int beginYmd, int endYmd){

        ZoneId zoneId = Stocks.getZoneId(stock);
        String filesPath = StockPaths.getVolumePowerFilesPath(stock.getStockId(), "1d");


        List<VolumePowerDaily> list = new ArrayList<>();
        StrCallback callback = str -> list.add(VolumePowerDaily.make(str));

        TimeLines.load(filesPath,  YmdUtil.getTime(beginYmd, zoneId), YmdUtil.getTime(endYmd,zoneId) + (Times.DAY_1 - 1L),TimeName.getDefaultType(Times.DAY_1), TimeLine.JSON,callback);
        return list.toArray(new VolumePowerDaily[0]);

    }

    public static VolumePowerDaily [] getVolumePower(VolumePowerDaily [] lastDailies, Stock stock, int beginYmd, int endYmd){
        if (lastDailies == null || lastDailies.length == 0) {
            return getVolumePower(stock, beginYmd, endYmd);
        }

        List<VolumePowerDaily> list = new ArrayList<>();
        int end = lastDailies.length - 1;

        for (int i = 0; i < end; i++) {
            VolumePowerDaily daily = lastDailies[i];
            int ymd = daily.getYmd();
            if (ymd < beginYmd) {
                continue;
            }
            if (ymd > endYmd) {
                continue;
            }
            list.add(daily);
        }

        int newBeginYmd = lastDailies[lastDailies.length - 1].getYmd();

        if (newBeginYmd < beginYmd) {
            newBeginYmd = beginYmd;
        }

        if (newBeginYmd >= endYmd) {
            return list.toArray(new VolumePowerDaily[0]);
        }

        VolumePowerDaily[] dailies = getVolumePower(stock, newBeginYmd, endYmd);
        Collections.addAll(list, dailies);

        return list.toArray(new VolumePowerDaily[0]);
    }

    public static void main(String[] args) {
        Stock stock = Stocks.getStock("KOR_005930");

        ZoneId zoneId = Stocks.getZoneId(stock);

        StockInvestorDaily[] dailies = getInvestor(stock, 20240901, 20240930);
        System.out.println(dailies.length);
        dailies = getInvestor(dailies, stock, 20240903, 20241004);
        System.out.println(dailies.length);
        for(StockInvestorDaily daily : dailies){
            System.out.println(YmdUtil.getYmd(daily.getTime(), zoneId)  + " " + daily);
        }
    }

}
