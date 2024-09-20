package io.runon.stock.trading.country.kor;

import com.seomse.commons.config.Config;
import com.seomse.commons.utils.string.Check;
import com.seomse.commons.utils.time.YmdUtil;
import com.seomse.jdbc.JdbcQuery;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.StockData;
import io.runon.stock.trading.data.StockDataManager;
import io.runon.trading.TradingTimes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 한국시장 관련처리
 * @author macle
 */
public class KorStocks {

    public static final String [] TARGET_EXCHANGES = {
            "KOSPI"
            , "KOSDAQ"
    };

    public static Stock [] getGeneralStocks(){
        return getGeneralStocks(YmdUtil.now(TradingTimes.KOR_ZONE_ID));
    }


    public static Stock [] getGeneralStocks(String standardYmd){
        StockData stockData = StockDataManager.getInstance().getStockData();
        Stock [] stocks ;

        if(YmdUtil.isNow(standardYmd, TradingTimes.KOR_ZONE_ID)){
            stocks = stockData.getStocks(TARGET_EXCHANGES);
        }else{
            stocks = stockData.getStocks(TARGET_EXCHANGES, standardYmd);
        }

        //주식종료 분석
        List<Stock> list = new ArrayList<>();

        //일반주식목록
        for(Stock stock : stocks){
            String type = stock.getStockType();
            if(!type.equals("STOCK")){
                continue;
            }

            if(Check.isNumberIn(stock.getNameKo())){
                continue;
            }
            if(stock.getNameKo().contains("스팩")){
                continue;
            }

            if(stock.getSymbol().equals("0")){
                list.add(stock);
            }
        }


        stocks = list.toArray(new Stock[0]);
        list.clear();
        return stocks;
    }


    public static void updateStockType(){
        String [] types = {"STOCK"};

        StockData stockData = StockDataManager.getInstance().getStockData();
        Stock [] stocks = stockData.getAllStocks(TARGET_EXCHANGES, types);

        Map<String, Stock> symbolMap = Stocks.makeSymbolMap(stocks);

        for(Stock stock : stocks){
            if(isPreferred(symbolMap, stock.getSymbol(), stock.getNameKo())){
                JdbcQuery.execute("update stock set stock_type='STOCK_PREFERRED' where stock_id='" + stock.getStockId() +"'");
                continue;
            }

            if(stock.getNameKo().contains("스팩")){
                JdbcQuery.execute("update stock set stock_type='SPAC' where stock_id='" + stock.getStockId() +"'");
            }
        }

        //
    }


    public static boolean isPreferred(Map<String, Stock> symnolMap, String symbol, String name){

        if(symbol.length() > 1 && !symbol.endsWith("0")){
            String searchSymbol = symbol.substring(0, symbol.length()-1) + "0";
            Stock searchStock = symnolMap.get(searchSymbol);
            if(searchStock != null){
                if(name.contains(searchStock.getNameKo()) && name.contains("우")){
                    return true;
                }
            }
        }

        if(name.endsWith("우") && name.length() > 1){
            char numCheck = name.charAt(name.length()-2);
            if(Check.isNumber(numCheck)){
                return true;
            }

            if( !symbol.endsWith("0")){
                return true;
            }

//            if(symbol.endsWith("5") || symbol.endsWith("K") || symbol.endsWith("7") || symbol.endsWith("E") || symbol.endsWith("C")){
//                return true;
//            }
        }

        if(name.endsWith("우B")){
            return true;
        }


        if(name.endsWith(")") && name.length() > 1){
            int index = name.lastIndexOf("(");
            if(index != -1){
                String checkStr = name.substring(index +1, name.length()-1);
                if(checkStr.contains("우")){
                    return true;
                }
            }
        }


        return false;
    }

    public static void main(String[] args) {
        Config.getConfig("");
//        StockData stockData = StockDataManager.getInstance().getStockData();
//
//
//        System.out.println(stockData.getAllStocks(TARGET_EXCHANGES).length);

        String name = "미래에셋대우스팩 5호";
        System.out.println(Check.isNumberIn(name));

    }

}