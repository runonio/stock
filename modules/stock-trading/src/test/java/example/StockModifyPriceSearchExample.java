package example;

import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.modify.StockModifyPriceSearch;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.CandlePreviousCandle;
import io.runon.trading.technical.analysis.candle.CandlePreviousCandles;

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
    public static void main(String[] args) {
        StockModifyPriceSearch search = new StockModifyPriceSearch(CountryCode.KOR);
        search.search();

        Map<String, CandlePreviousCandles> map = search.getMap();

        Set<String> keys = map.keySet();

        for(String key : keys){
            System.out.println(key +", " +  map.get(key).getArray().length);

            CandlePreviousCandle[] array = map.get(key).getArray();
            System.out.println(array[array.length-1]);
            System.out.println(YmdUtil.getYmd(array[array.length-1].getCandle().getOpenTime(), TradingTimes.KOR_ZONE_ID));

            break;

        }

        System.out.println(keys.size());

    }
}
