package example;

import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.modify.StockModifyPriceSearch;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.CandlePreviousCandle;
import io.runon.trading.technical.analysis.candle.CandlePreviousCandles;

import java.time.ZoneId;
import java.util.Map;
import java.util.Set;

/**
 * 수정주가 검색
 * 데이터 검증
 * 수정주가  검색 예제
 * 수정주가가 발생하면 수정주가가 반영된 캔들을 다시 받아야함
 * @author macle
 */
public class StockModifyPriceSearchExample {

    public static void view( CountryCode countryCode){
        ZoneId zoneId = TradingTimes.getZoneId(countryCode);
        StockModifyPriceSearch search = new StockModifyPriceSearch(countryCode);
        search.search(null);

        Map<String, CandlePreviousCandles> map = search.getMap();

        Set<String> keys = map.keySet();


        for(String key : keys){

            System.out.println("===================================================================================");
            Stock stock = Stocks.getStock(key) ;

            CandlePreviousCandle[] array = map.get(key).getArray();
            System.out.println(stock.getNameKo() +", " + stock.getSymbol() + ", search length: " + array.length);


            for(CandlePreviousCandle candle : array){

                System.out.println(YmdUtil.getYmd(candle.getCandle().getOpenTime(), zoneId) + ", " + candle.getCandle().getClose().toPlainString() +", " +candle.getCandle().getPrevious().toPlainString()
                        +", " + candle.getPreviousCandle().getClose().toPlainString());

            }
        }

        System.out.println(map.size());
    }

    public static void main(String[] args) {

        CountryCode countryCode = CountryCode.KOR;
        view(countryCode);

    }
}
