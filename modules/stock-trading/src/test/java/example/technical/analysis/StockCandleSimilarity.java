package example.technical.analysis;

import io.runon.commons.data.BigDecimalArray;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.daily.StockDataLoad;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.similarity.TradingSimilarity;

import java.math.BigDecimal;

/**
 * @author macle
 */
public class StockCandleSimilarity {
    public static void main(String[] args) {

        int beginYmd = 20150101;
        int endYmd = YmdUtil.nowInt(TradingTimes.KOR_ZONE_ID);

        Stock source = Stocks.getStock("KOR_433330");
        Stock target = Stocks.getStock("KOR_488500");

        TradeCandle [] sourceCandles = StockDataLoad.getCandle( source, beginYmd, endYmd);

        TradeCandle [] targetCandles = StockDataLoad.getCandle( target, beginYmd, endYmd);

        BigDecimalArray result = TradingSimilarity.changeSimilarity(sourceCandles, targetCandles, new BigDecimal("0.2"), new BigDecimal("100000000"));

        System.out.println(result);

    }
}
