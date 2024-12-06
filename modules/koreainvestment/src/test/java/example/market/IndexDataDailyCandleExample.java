package example.market;

import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentMarketApi;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * @author macle
 */
public class IndexDataDailyCandleExample {
    public static void main(String[] args) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentMarketApi marketApi = api.getMarketApi();

        TradeCandle [] candles = marketApi.getIndexCandles("KOSPI", "20200101","20240626");
        for(TradeCandle tradeCandle:  candles){
            System.out.println(YmdUtil.getYmd(tradeCandle.getOpenTime(), TradingTimes.KOR_ZONE_ID));

            System.out.println(tradeCandle);
        }

        System.out.println(candles.length);
    }
}
