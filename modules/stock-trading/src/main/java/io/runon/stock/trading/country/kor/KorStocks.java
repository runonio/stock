package io.runon.stock.trading.country.kor;

import io.runon.commons.utils.string.Check;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.jdbc.JdbcQuery;
import io.runon.stock.trading.*;
import io.runon.stock.trading.data.StockData;
import io.runon.stock.trading.data.StockDataManager;
import io.runon.trading.TradingTimes;

import java.util.*;

/**
 * 한국시장 관련처리
 * @author macle
 */
public class KorStocks {

    //투자자별 매매동향
    public static final int INVESTOR_DAY_GAP = -1;

    //공매도
    public static final int SHORT_SELLING_DAY_GAP = -2;

    //대차잔고
    public static final int STOCK_LOAN_DAY_GAP = -1;

    //주식별신용
    public static final int STOCK_CREDIT_LOAN_DAY_GAP = -1;


    //프로그램 매매는 day 정보 확인필요.
    public static final int PROGRAM_DAY_GAP = 0;

    public static final int VOLUME_POWER_DAY_GAP = 0;


    public static final String [] TARGET_EXCHANGES = {
            "KOSPI"
            , "KOSDAQ"
    };

    public static Stock [] getGeneralStocks(){

        return getGeneralStocks(Stocks.getStocks(TARGET_EXCHANGES));
    }

    //상장폐지포함
    public static Stock [] getGeneralStocksInDelisted(){
        StockData stockData = StockDataManager.getInstance().getStockData();
        String [] types ={
                "STOCK"
        };

        return getGeneralStocks( stockData.getAllStocks(TARGET_EXCHANGES, types));
    }


    public static Stock [] getEtfsInDelisted(){
        StockData stockData = StockDataManager.getInstance().getStockData();
        String [] types ={
                "ETF"
        };

        return stockData.getAllStocks(TARGET_EXCHANGES, types);
    }


    /**
     * 일반 주식과 ETF, 벡테스팅용 데이터 로드에사용
     * @return 일반 주식과 ETF
     */
    public static Stock [] getGeneralStocksEtfInDelisted(){


        List<Stock> list = new ArrayList<>();
        Set<String> check = new HashSet<>();
        Stock [] stocks = getGeneralStocksInDelisted();
        for(Stock stock : stocks){
            list.add(stock);
            check.add(stock.getStockId());
        }


        stocks = getEtfsInDelisted();
        for(Stock stock : stocks) {
            if(check.contains(stock.getStockId())){
                continue;
            }
            list.add(stock);
        }

        Stock [] result = list.toArray(new Stock[list.size()]);
        list.clear();
        check.clear();
        return result;
    }



    public static Stock [] getGeneralStocks(Stock [] stocks){
        return getGeneralStocks(stocks, null);
    }


    public static Stock [] getGeneralStocks(Stock [] stocks, Integer baseYmd){

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



            if(!stock.getSymbol().equals("0")){
                list.add(stock);
            }
        }
        stocks = list.toArray(new Stock[0]);
        list.clear();
        return stocks;
    }

    public static StockMap makeStockMap(String baseYmd){
        StockData stockData = StockDataManager.getInstance().getStockData();
        Stock [] stocks ;
        if(YmdUtil.isNow(baseYmd, TradingTimes.KOR_ZONE_ID)){
            stocks = stockData.getStocks(TARGET_EXCHANGES);
        }else{
            stocks = stockData.getStocks(TARGET_EXCHANGES, baseYmd);
        }

        return new StockMap(stocks);
    }

    /**
     * 대상 etf를 찾기위한 db작업 보조용 메소드
     * @param inName 이름이 포함하고 있는값 대
     * @return 중목과 시가총액, 시총이 높은순우로 정렬된값
     */
    public static StockNumber[] searchEtfNames(String inName){
        String [] types ={"ETF"};

        Stock [] stocks = Stocks.getStocks(TARGET_EXCHANGES, types);

        List<Stock> list = new ArrayList<>();

        for(Stock stock : stocks){
            //한글이름검사
            String name = stock.getNameKo();
            if(name == null){
                continue;
            }

            if(name.contains(inName)){
                list.add(stock);
                continue;
            }
            
            //영문이름검사
            name = stock.getNameEn();
            if(name == null){
                continue;
            }
            
            if(name.contains(inName)){
                list.add(stock);
            }
        }

        StockNumber [] stockNumbers = Stocks.getMarketCap(list.toArray(new Stock[0]));
        list.clear();

        return stockNumbers;
    }

    public static StockNumber[] searchEtfNames(String [] inNames){
        String [] types ={"ETF"};

        if(inNames == null || inNames.length == 0){
            return new StockNumber[0];
        }

        Stock [] stocks = Stocks.getStocks(TARGET_EXCHANGES, types);

        List<Stock> list = new ArrayList<>();

        for(Stock stock : stocks){
            //한글이름검사
            String name = stock.getNameKo();
            if(name == null){
                continue;
            }
            int inCount = 0;
            for(String inName: inNames) {

                if (name.contains(inName)) {
                    inCount++;
                }
            }

            if(inCount ==  inNames.length) {
                list.add(stock);
                continue;
            }

            //영문이름검사
            name = stock.getNameEn();
            if(name == null){
                continue;
            }
            inCount = 0;
            for(String inName: inNames) {

                if (name.contains(inName)) {
                    inCount++;
                }
            }

            if(inCount ==  inNames.length) {
                list.add(stock);
              
            }
        }

        StockNumber [] stockNumbers = Stocks.getMarketCap(list.toArray(new Stock[0]));
        list.clear();

        return stockNumbers;
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

    public static Stock [] getBondLongEtfs(StockMap stockMap){
        return StockGroups.getGroupStocks(stockMap, "kor_bond_long_etf");

    }


    public static Stock [] getBondShortEtfs(StockMap stockMap){
        //etf는 종류별로 동일한 자산을 취급하지 않아야 분산이 가능하므로 정리된 자료를 이용한다.
        return StockGroups.getGroupStocks(stockMap, "kor_bond_short_etf");
    }


    public static Stock [] getIndexLongEtfs(StockMap stockMap){
        //etf는 종류별로 동일한 자산을 취급하지 않아야 분산이 가능하므로 정리된 자료를 이용한다.
        return StockGroups.getGroupStocks(stockMap, "kor_index_long_etf");
    }


    public static Stock [] getIndexShortEtfs(StockMap stockMap){
        //etf는 종류별로 동일한 자산을 취급하지 않아야 분산이 가능하므로 정리된 자료를 이용한다.
        return StockGroups.getGroupStocks(stockMap, "kor_index_short_etf");
    }

    public static Stock [] getCommoditiesLongEtfs(StockMap stockMap){
        //etf는 종류별로 동일한 자산을 취급하지 않아야 분산이 가능하므로 정리된 자료를 이용한다.
        return StockGroups.getGroupStocks(stockMap, "kor_commodities_long_etf");
    }

    public static Stock [] getCommoditiesShortEtfs(StockMap stockMap){
        //etf는 종류별로 동일한 자산을 취급하지 않아야 분산이 가능하므로 정리된 자료를 이용한다.
        return StockGroups.getGroupStocks(stockMap, "kor_commodities_short_etf");
    }


    public static Stock [] getCurrenciesLongEtfs(StockMap stockMap){
        //etf는 종류별로 동일한 자산을 취급하지 않아야 분산이 가능하므로 정리된 자료를 이용한다.
        return StockGroups.getGroupStocks(stockMap, "kor_currencies_long_etf");
    }

    public static Stock [] getCurrenciesShortEtfs(StockMap stockMap){
        //etf는 종류별로 동일한 자산을 취급하지 않아야 분산이 가능하므로 정리된 자료를 이용한다.
        return StockGroups.getGroupStocks(stockMap, "kor_currencies_short_etf");
    }
}