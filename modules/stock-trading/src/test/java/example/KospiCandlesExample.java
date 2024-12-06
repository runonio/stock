package example;

import io.runon.commons.utils.time.YmdUtil;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;
/**
 * @author macle
 */
public class KospiCandlesExample {
    public static void main(String[] args) {
        TradeCandle [] kospiCandles = CsvCandle.loadDailyCandles("indices\\major\\candle\\KOSPI\\1d", null,20240901, 20240930, TradingTimes.KOR_ZONE_ID);

        for(TradeCandle candle : kospiCandles){
            System.out.println(YmdUtil.getYmd(candle.getOpenTime(), TradingTimes.KOR_ZONE_ID) + candle);
        }

        System.out.println("========================================");

        kospiCandles = CsvCandle.loadDailyCandles("indices\\major\\candle\\KOSPI\\1d", kospiCandles,20240910, 20241007, TradingTimes.KOR_ZONE_ID);

        for(TradeCandle candle : kospiCandles){
            System.out.println(YmdUtil.getYmd(candle.getOpenTime(), TradingTimes.KOR_ZONE_ID) + candle);
        }

    }
}
