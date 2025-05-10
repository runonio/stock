package io.runon.stock.trading.financial.statements.kor;

import io.runon.commons.config.Config;
import io.runon.commons.exception.InvalidParameterException;
import io.runon.jdbc.JdbcQuery;
import io.runon.stock.trading.financial.statements.FinancialStatements;
import io.runon.trading.CountryCode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
//        Set<String> keys = map.keySet();
//        for(String key : keys){
//
//            Map<String, BigDecimal> dataMap = map.get(key);
//            Set<String> dataKeys = dataMap.keySet();
//            for(String dataKey : dataKeys){
//                System.out.println(key +"," +dataKey +", " + dataMap.get(dataKey).toPlainString() );
//            }
//
//        }

        
        return getFinancialStatements(map);
    }

    public static FinancialStatements getFinancialStatements(Map<String, Map<String, BigDecimal>> map){

        Set<String> keys = map.keySet();
        for(String key : keys){

            Map<String, BigDecimal> dataMap = map.get(key);
            Set<String> dataKeys = dataMap.keySet();
            for(String dataKey : dataKeys){
                System.out.println(key +"," +dataKey +", " + dataMap.get(dataKey).toPlainString() );
            }

        }


        return null;
    }


    public static String getFnlttSinglAcntAll(String stockId, String param){
        return  JdbcQuery.getResultOne("select data_value from stock_api_data where stock_id='" + stockId+"' and api_url='opendart.fss.or.kr/api/fnlttSinglAcntAll.json' and api_param='" +param+"'" );
    }

    public static Map<String, Map<String, BigDecimal>> makeFnlttSinglAcntAllMap(String stockId, String param) {
        return makeFnlttSinglAcntAllMap(getFnlttSinglAcntAll(stockId, param));
    }


    public static Map<String, Map<String, BigDecimal>> makeFnlttSinglAcntAllMap(String jsonText){


        Map<String, Map<String, BigDecimal>> map = new HashMap<>();

        JSONArray array = new JSONArray(jsonText);
        for (int i = 0; i <array.length() ; i++) {
            JSONObject row = array.getJSONObject(i);

            if(row.isNull("thstrm_amount")){
                continue;
            }

            String categoryName = row.getString("sj_nm");

            String name = row.getString("account_nm");

            String value = row.getString("thstrm_amount");
            if(value.equals("") || value.equals("-") || value.equals("0")){
                continue;
            }
            Map<String, BigDecimal> dataMap = map.computeIfAbsent(categoryName, k -> new LinkedHashMap<>());
            dataMap.put(name, new BigDecimal(value));

        }

        return map;
    }




    public static void main(String[] args) {
        Config.getConfig("");

//        OFS:재무제표, CFS:연결재무제표

//        Map<String, Map<String, BigDecimal>> map = makeFnlttSinglAcntAllMap("KOR_005930", "2024,4,CFS");
//
//        Set<String> keys = map.keySet();
//        for(String key : keys){
//
//            Map<String, BigDecimal> dataMap = map.get(key);
//            Set<String> dataKeys = dataMap.keySet();
//            for(String dataKey : dataKeys){
//                System.out.println(key +"," +dataKey +", " + dataMap.get(dataKey).toPlainString() );
//            }
//        }



        getFinancialStatements("005930","2024",1,true);



//        System.out.println(getFnlttSinglAcntAll("KOR_005930", "2024,2"));
    }
}
