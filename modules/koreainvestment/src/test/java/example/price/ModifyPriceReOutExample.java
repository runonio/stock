package example.price;

import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.securities.firm.api.kor.koreainvestment.SpotDailyCandleOut;
import io.runon.stock.securities.firm.api.kor.koreainvestment.UasSpotDailyCandleOut;
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
 * @author macle
 */
public class ModifyPriceReOutExample {

    public static void usaDailyCandleReOut(){

        StockModifyPriceSearch search = new StockModifyPriceSearch(CountryCode.USA);
        search.search();

        UasSpotDailyCandleOut out = new UasSpotDailyCandleOut();
        Map<String, CandlePreviousCandles> map = search.getMap();
        Set<String> keys = map.keySet();


        for(String key : keys){
            Stock stock = Stocks.getStock(key) ;
            try{Thread.sleep(1000L);}catch(Exception ignore){}

            out.reOut(stock);
        }
    }

    public static void korDailyCandleReOut(){
        SpotDailyCandleOut out = new SpotDailyCandleOut();
        StockModifyPriceSearch search = new StockModifyPriceSearch(CountryCode.KOR);
        search.search();

        Map<String, CandlePreviousCandles> map = search.getMap();
        Set<String> keys = map.keySet();


        for(String key : keys){
            Stock stock = Stocks.getStock(key) ;
            try{Thread.sleep(1000L);}catch(Exception ignore){}

            out.reOut(stock);
        }
    }

    //수정주가 데이터 검색
    public static void view( CountryCode countryCode){
        //거래정지 종목에 한해서 데이터가 맞지않는 경우가 발생한다.
        ZoneId zoneId = TradingTimes.getZoneId(countryCode);
        StockModifyPriceSearch search = new StockModifyPriceSearch(countryCode);
        search.search();

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

//        view(CountryCode.KOR);
//        korDailyCandleReOut();


//        view(CountryCode.USA);
//        usaDailyCandleReOut();
    }
}
