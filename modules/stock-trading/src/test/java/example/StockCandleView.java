package example;

import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.candle.StockCandles;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * @author macle
 */
public class StockCandleView {
    public static void main(String[] args) {
        Stock stock = Stocks.getStock("KOR_217330");

        int baseYmd = YmdUtil.nowInt(TradingTimes.KOR_ZONE_ID);
        int beginYmd = YmdUtil.getYmdInt(baseYmd, - 30);

        TradeCandle [] candles = StockCandles.getDailyCandles(stock, 20230104, 20230110);

        for(TradeCandle candle :candles){
            System.out.println(YmdUtil.getYmd(candle.getOpenTime(), TradingTimes.KOR_ZONE_ID));
            System.out.println(candle);
        }

    }
}
