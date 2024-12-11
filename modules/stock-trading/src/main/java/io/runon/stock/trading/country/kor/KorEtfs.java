package io.runon.stock.trading.country.kor;

import io.runon.commons.data.BigDecimalArray;
import io.runon.commons.data.IdArray;
import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.technical.analysis.clustering.StockClusteringData;
import io.runon.stock.trading.technical.analysis.similarity.StockSimSearchCandle;
import io.runon.stock.trading.technical.analysis.similarity.StockSimSearchData;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.Exchanges;
import io.runon.trading.technical.analysis.similarity.TradingSimilarity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author macle
 */
public class KorEtfs {


    public static void kospi( ){
        kospi(YmdUtil.now(TradingTimes.KOR_ZONE_ID) , 150);
    }

    public static StockSimSearchData kospi(String baseYmd, int dayRange ){
        //코스피 etf 관련작업
        //성적높은 etf뽑아보기

        //kodex 200;
        Stock baseStock = Stocks.getStock("KOR_069500");


        String beginYmd = YmdUtil.getYmd(baseYmd, dayRange*-1);

        long beginTime = YmdUtil.getTime(beginYmd, TradingTimes.KOR_ZONE_ID);
        long endTime = YmdUtil.getTime(baseYmd, TradingTimes.KOR_ZONE_ID) + Times.DAY_1;

        StockSimSearchCandle baseCandle = new StockSimSearchCandle(baseStock);
        baseCandle.setInterval("1d");
        baseCandle.setBeginTime(beginTime);
        baseCandle.setEndTime(endTime);

        String [] types ={
                "ETF"
        };

        Stock [] stocks = Stocks.getStocks(Exchanges.getDefaultExchanges(CountryCode.KOR), types);

        List<Stock> targetStocklist = new ArrayList<>();
        targetStocklist.add(baseStock);

        for(Stock stock : stocks){
            String name = stock.getNameKo();
            if(stock.getStockId().equals(baseStock.getStockId())){
                continue;
            }

            if(name.contains("코스피") && !name.contains("인버스")){
                targetStocklist.add(stock);
            }
        }

        Stock [] targetStocks = targetStocklist.toArray(new Stock[0]);
        Map<String, Stock> map = Stocks.makeMap(targetStocks);


        StockClusteringData [] targets = new StockClusteringData[targetStocks.length];

        for (int i = 0; i <targets.length ; i++) {
            Stock targetStock = targetStocks[i];
            StockClusteringData target = new StockClusteringData(targetStock);
            target.setInterval("1d");
            target.setBeginTime(beginTime);
            target.setEndTime(endTime);
            targets[i] = target;
        }

        int minSize ;

        if(dayRange > 150){
            minSize = 80;
        }else if( dayRange > 100){
            minSize = 50;
        }else if( dayRange > 60){
            minSize = 30;
        }else if( dayRange > 20){
            minSize = 10;
        }else{
            minSize = 1;
        }

        IdArray<BigDecimalArray> [] searchDataArray =  TradingSimilarity.search(baseCandle, targets, new BigDecimal("0.2"), minSize, new BigDecimal("70") );
        StockSimSearchData stockSimSearchData = new StockSimSearchData();
        stockSimSearchData.setSearchArray(searchDataArray);
        stockSimSearchData.setTargetStocks(targetStocks);
        stockSimSearchData.setMap(map);
        return stockSimSearchData;

//        for(IdArray<BigDecimalArray> searchData: searchDataArray){
//            System.out.println("============\n" + searchData.getId()+ "," +map.get(searchData.getId()).getNameKo() +"\n"+ searchData.getArray());
//        }
//
//
//        System.out.println(searchDataArray.length);
    }


    public static void kosdaq(){

    }

    public static void korBond30(){

    }

    public static void usaBond30(){

    }

    public static void snp500(){

    }

    public static void nasdaq(){

    }

    public static void main(String[] args) {
        kospi();
    }
}
