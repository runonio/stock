import io.runon.commons.config.Config;
import io.runon.stock.securities.firm.api.kor.ls.LsFuturesDailyCandleOut;

/**
 * @author macle
 */
public class LsFuturesDailyExample {
    public static void main(String[] args) {
//        LsApi api = LsApi.getInstance();
//        api.updateAccessToken();
//        String text = api.getFuturesApi().getDailyFuturesJsonText("401VC6CS","20240920", YmdUtils.now(TradingTimes.KOR_ZONE_ID));

//        TradeCandle [] candles = api.getFuturesApi().getDailyCandles("111VC000","20211210", YmdUtils.now(TradingTimes.KOR_ZONE_ID));
//        for(TradeCandle candle :candles){
//            System.out.println(candle);
//        }

//        System.out.println( candles.length);


        Config.getConfig("");

        LsFuturesDailyCandleOut futuresDailyCandleOut = new LsFuturesDailyCandleOut();
        futuresDailyCandleOut.out();

    }
}
