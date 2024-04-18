package example.price;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * 기간별 가격 데이터와 정보
 * 한국투자증권 오리지날 데이터
 * @author macle
 */
public class PricePeriodJsonTextOut {
    public static void main(String[] args) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();

        KoreainvestmentPeriodDataApi periodDataApi = api.getPeriodDataApi();

        String text = periodDataApi.getPeriodDataJsonText("069500","D","20240101","20240415",true);

        System.out.println(text);
        TradeCandle [] candles = KoreainvestmentPeriodDataApi.getCandles(text);
        for(TradeCandle candle : candles){
            System.out.println(candle);
        }
        System.out.println(candles.length);


//        System.out.println(candles[0]);
//        System.out.println(candles[1]);
//        String candleCsv = CsvCandle.value(candles[0]);
//
//        System.out.println(candleCsv);
//
//        System.out.println(candles[1]);
//        System.out.println(CsvCandle.make(candleCsv));



    }
}
