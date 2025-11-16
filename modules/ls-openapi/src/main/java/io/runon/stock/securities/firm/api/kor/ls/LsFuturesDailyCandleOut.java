package io.runon.stock.securities.firm.api.kor.ls;

import io.runon.commons.utils.time.YmdUtils;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.Futures;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.data.management.FuturesPathLastTime;
import io.runon.trading.data.management.KorFuturesDailyOut;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * ls증권 open api
 * @author macle
 */
public class LsFuturesDailyCandleOut extends KorFuturesDailyOut {

    private final LsApi api = LsApi.getInstance();
    private final LsFuturesApi futuresApi = api.getFuturesApi();

    public LsFuturesDailyCandleOut() {
        super(FuturesPathLastTime.CANDLE, PathTimeLine.CSV);
    }


    @Override
    public String[] getLines(Futures futures, String beginYmd, String endYmd) {

        TradeCandle[] candles = futuresApi.getDailyCandles(futures.getSymbol(), beginYmd, endYmd);
        return CsvCandle.lines(candles);
    }

    @Override
    public boolean isListed() {
        return true;
    }

    @Override
    public int getNextDay() {
        return 400;
    }


    @Override
    public void sleep() {
        api.periodSleep();
    }

    public void out(){
        String nowYmd = YmdUtils.now(TradingTimes.KOR_ZONE_ID);
        dailyOut.out(nowYmd);
    }

}
