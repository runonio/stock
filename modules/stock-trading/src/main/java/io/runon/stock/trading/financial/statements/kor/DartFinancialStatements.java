package io.runon.stock.trading.financial.statements.kor;

import io.runon.commons.config.Config;
import io.runon.commons.exception.InvalidParameterException;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.jdbc.JdbcQuery;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.financial.statements.FinancialStatements;
import io.runon.trading.BigDecimals;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.*;

/**
 * dart 재무재표 관련 처리
 * @author macle
 */
public class DartFinancialStatements {

    /**
     *
     * @param idOrSymbol 종목 아이디 또는 symbol
     * @param year 년도
     * @param quarter 분기 1,2,3,4
     * @param isConsolidated 연결제무제표 여부
     * @return 제무재표
     */
    public static FinancialStatements getFinancialStatements(String idOrSymbol, String year,int quarter, boolean isConsolidated ){

        if( !(quarter > 0 && quarter<5 )){
            throw new InvalidParameterException("quarter in 1~4");
        }

        String month ;
        if(quarter == 1){
            month = "03";
        }else if(quarter == 2){
            month = "06";
        }else if(quarter == 3){
            month = "09";
        }else{
            month = "12";
        }

        String stockId = idOrSymbol;
        String country = CountryCode.KOR + "_";

        if(!stockId.startsWith(country)){
            stockId = country + stockId;
        }
        String param;

        if(isConsolidated){
            param = year +"," + quarter + ",CFS" ;
        }else{
            param = year +"," + quarter + ",OFS" ;
        }

        Map<String, Map<String, BigDecimal>> map = makeFnlttSinglAcntAllMap(stockId, param);
        if(map.isEmpty()){
            return null;
        }

        FinancialStatements financialStatements = getFinancialStatements(map);

        if(financialStatements == null || !financialStatements.isIn()){
            return null;
        }
        financialStatements.setYm(year+month);

        return financialStatements;
    }

    public static FinancialStatements getFinancialStatements(Map<String, Map<String, BigDecimal>> map){

        if(map == null || map.isEmpty()){
            return null;
        }

        FinancialStatements financialStatements = new FinancialStatements();

        //손익계산서
        Map<String, BigDecimal> dataMap = map.get("손익계산서");
        Set<String> keys = map.keySet();

        financialStatements.setSales(getSales(map));
        financialStatements.setOperatingProfit(getOperatingProfit(map));
        financialStatements.setNetProfit(getNetProfit(map));
        financialStatements.setAssets( getAssets(map));
        financialStatements.setLiabilities( getLiabilities(map));
        financialStatements.setCapital(getCapital(map));
        financialStatements.setOperatingCashFlow(getOperatingCashFlow(map));
        financialStatements.setCashEquivalents(getCashEquivalents(map));

        return financialStatements;
    }

    //매출액 설정
    public static BigDecimal getSales( Map<String, Map<String, BigDecimal>> map){

        String [] mapKeys = {
                "손익계산서"
                , "포괄손익계산서"
        };

        String [] numKeys = {
                "매출액"
                , "영업수익"
                , "수익(매출액)"
        };

        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static BigDecimal getOperatingProfit( Map<String, Map<String, BigDecimal>> map) {
        String [] mapKeys = {
                "손익계산서"
                , "포괄손익계산서"
        };

        String [] numKeys = {
                "영업이익"
                , "영업이익(손실)"
        };


        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static BigDecimal getNetProfit( Map<String, Map<String, BigDecimal>> map) {

        String [] mapKeys = {
                "손익계산서"
                , "포괄손익계산서"
        };

        String [] numKeys = {
                "분기순이익(손실)"
                , "분기순손익"
                , "당기순이익(손실)"
        };

        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static BigDecimal getAssets( Map<String, Map<String, BigDecimal>> map) {
        //자산

        String [] mapKeys = {
                "재무상태표"
        };

        String [] numKeys = {
                 "자산총계"
        };


        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static BigDecimal getLiabilities( Map<String, Map<String, BigDecimal>> map) {

        String [] mapKeys = {
                "재무상태표"
        };

        String [] numKeys = {
                "부채총계"
        };

        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static BigDecimal getCapital( Map<String, Map<String, BigDecimal>> map) {

        String [] mapKeys = {
                "재무상태표"
        };

        String [] numKeys = {
                "자본총계"
        };


        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static BigDecimal getOperatingCashFlow( Map<String, Map<String, BigDecimal>> map) {
        //영업활동 현금흐름
        String [] mapKeys = {
                "현금흐름표"
        };

        String [] numKeys = {
                "영업활동현금흐름"
                , "영업활동으로인한현금흐름"
        };



        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static BigDecimal getCashEquivalents( Map<String, Map<String, BigDecimal>> map) {
        //현금성자산

        String [] mapKeys = {
                "재무상태표"
        };

        String [] numKeys = {
                "현금및현금성자산"
        };

        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static String getFnlttSinglAcntAll(String stockId, String param){
        return  JdbcQuery.getResultOne("select data_value from stock_api_data where stock_id='" + stockId+"' and api_url='opendart.fss.or.kr/api/fnlttSinglAcntAll.json' and api_param='" +param+"'" );
    }

    public static Map<String, Map<String, BigDecimal>> makeFnlttSinglAcntAllMap(String stockId, String param) {
        return makeFnlttSinglAcntAllMap(getFnlttSinglAcntAll(stockId, param));
    }

    public static Map<String, Map<String, BigDecimal>> makeFnlttSinglAcntAllMap(String jsonText){


        if(jsonText == null){
            return Collections.emptyMap();
        }

        Map<String, Map<String, BigDecimal>> map = new HashMap<>();

        JSONArray array = new JSONArray(jsonText);
        for (int i = 0; i <array.length() ; i++) {
            JSONObject row = array.getJSONObject(i);

            if(row.isNull("thstrm_amount")){
                continue;
            }

            String categoryName = row.getString("sj_nm");

            String name = row.getString("account_nm");

            name = name.trim();

            String value = row.getString("thstrm_amount");
            if(value.equals("") || value.equals("-") || value.equals("0")){
                continue;
            }
            Map<String, BigDecimal> dataMap = map.computeIfAbsent(categoryName, k -> new LinkedHashMap<>());
            dataMap.put(name, new BigDecimal(value));

        }

        return map;
    }


    public static void consoleView(String idOrSymbol, String year,int quarter, boolean isConsolidated){
        if( !(quarter > 0 && quarter<5 )){
            throw new InvalidParameterException("quarter in 1~4");
        }

        String stockId = idOrSymbol;
        String country = CountryCode.KOR + "_";

        if(!stockId.startsWith(country)){
            stockId = country + stockId;
        }

        String param;

        if(isConsolidated){
            param = year +"," + quarter + ",CFS" ;
        }else{
            param = year +"," + quarter + ",OFS" ;
        }

        Map<String, Map<String, BigDecimal>> map = makeFnlttSinglAcntAllMap(stockId, param);

        Set<String> keys = map.keySet();
        for(String key : keys){

            Map<String, BigDecimal> dataMap = map.get(key);
            Set<String> dataKeys = dataMap.keySet();
            for(String dataKey : dataKeys){
                System.out.println(key +"," +dataKey +", " + dataMap.get(dataKey).toPlainString() );
            }
        }
    }

    public static void searchNullIn(){
        int year = 2016;

        int maxYm = Integer.parseInt(YmdUtil.getYmd(YmdUtil.now(TradingTimes.KOR_ZONE_ID), -93).substring(0,6));

        String [] markets = {
                "KOSPI"
                , "KOSDAQ"
        };
        String [] types ={
                "STOCK"
        };

        Stock [] stocks = Stocks.getAllStocks(markets, types);

        for(Stock stock : stocks){
            outer:
            for(;;){
                for (int quarter = 1; quarter <=4 ; quarter++) {
                    int month = quarter*3;
                    String monthStr = Integer.toString(month);
                    if(monthStr.length() == 1){
                        monthStr = "0"+monthStr;
                    }

                    int ym = Integer.parseInt(year + monthStr);
                    if(ym > maxYm){
                        break outer;
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        Config.getConfig("");

//        OFS:재무제표(별도), CFS:연결재무제표

        consoleView("005930","2016",1,true);

//        System.out.println(getFnlttSinglAcntAll("KOR_005930", "2024,2"));


        FinancialStatements financialStatements =  getFinancialStatements("005930","2016",1,true);
        System.out.println(financialStatements);
    }
}
