package example.period;

import io.runon.commons.utils.time.YmdUtils;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * @author macle
 */
public class KorNxtCandleOut {
    public static void main(String[] args) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();

        KoreainvestmentPeriodDataApi dataApi = api.getPeriodDataApi();

        TradeCandle [] candles = dataApi.getNxtCandles("005930", "D",   "20250304", YmdUtils.now(TradingTimes.KOR_ZONE_ID), true);

        for(TradeCandle candle : candles){
            System.out.println(candle);
        }

        System.out.println(candles.length);

    }
}
