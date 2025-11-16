package example;

import io.runon.commons.config.Config;
import io.runon.commons.utils.time.YmdUtils;
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

        TradeCandle[] candles = StockCandles.getDailyCandles(stock, "KRX","20200101", "20241211");


//        for (int i =1; i < candles.length; i++) {
//            if(candles[i].getPrevious().compareTo(candles[i-1].getClose()) != 0){
//                System.out.println("=----------------------");
//                System.out.println(YmdUtils.getYmd(candles[i-1].getOpenTime(), TradingTimes.KOR_ZONE_ID) + ", " + candles[i-1].getClose().toPlainString() +", " +candles[i].getLockType());
//
//                System.out.println(YmdUtils.getYmd(candles[i].getOpenTime(), TradingTimes.KOR_ZONE_ID) + ", " + candles[i].getClose().toPlainString() +", " + candles[i].getPrevious().toPlainString());
//
//            }
//        }
//

        for(TradeCandle candle :candles){
            System.out.println(YmdUtils.getYmd(candle.getOpenTime(), TradingTimes.KOR_ZONE_ID) + ", " + candle.getClose().toPlainString() +", " + candle.getPrevious().toPlainString());
        }





//        Stocks.getDailyCandles()
    }
}
