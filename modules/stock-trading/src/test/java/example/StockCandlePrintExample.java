package example;

import com.seomse.commons.config.Config;
import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.candle.StockCandles;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * @author macle
 */
public class StockCandlePrintExample {
    public static void main(String[] args) {

        Config.getConfig("");

        Stock stock = Stocks.getStock(CountryCode.KOR, "453850");

        TradeCandle[] candles = StockCandles.getDailyCandles(stock, "20200101", "20240909");


        for (int i =1; i < candles.length; i++) {
            if(candles[i].getPrevious().compareTo(candles[i-1].getClose()) != 0){
                System.out.println("=----------------------");
                System.out.println(YmdUtil.getYmd(candles[i-1].getOpenTime(), TradingTimes.KOR_ZONE_ID) + ", " + candles[i-1].getClose().toPlainString());

                System.out.println(YmdUtil.getYmd(candles[i].getOpenTime(), TradingTimes.KOR_ZONE_ID) + ", " + candles[i].getClose().toPlainString() +", " + candles[i].getPrevious().toPlainString());

            }
        }
//
//        for(TradeCandle candle :candles){
//            System.out.println(YmdUtil.getYmd(candle.getOpenTime(), TradingTimes.KOR_ZONE_ID) + ", " + candle.getClose().toPlainString() +", " + candle.getPrevious().toPlainString());
//        }





//        Stocks.getDailyCandles()
    }
}
