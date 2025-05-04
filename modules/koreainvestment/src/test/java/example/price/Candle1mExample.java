package example.price;

import io.runon.commons.utils.time.Times;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.TradeCandles;

/**
 * @author macle
 */
public class Candle1mExample {
    public static void main(String[] args) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();

        KoreainvestmentPeriodDataApi dataApi = api.getPeriodDataApi();

        TradeCandle [] candles = dataApi.get1mCandles("005930","NX","20250428");

//        for(TradeCandle candle : candles){
//            System.out.println(candle);
//            System.out.println(CsvCandle.value(candle));
//        }

        for(TradeCandle candle : candles){
            System.out.println(Times.ymdhm(candle.getTime(), TradingTimes.KOR_ZONE_ID));

        }

        System.out.println(candles[1].getVolume());


        System.out.println(TradeCandles.sumVolume(candles));
    }
}
