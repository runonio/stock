package io.runon.stock.trading.financial.statements;

import com.seomse.commons.config.Config;
import com.seomse.jdbc.JdbcQuery;
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

    public static String getFnlttSinglAcntAll(String stockId, String yq){
        return  JdbcQuery.getResultOne("select data_value from stock_api_data where stock_id='" + stockId+"' and api_url='opendart.fss.or.kr/api/fnlttSinglAcntAll.json' and api_param='" +yq+"'" );
    }

    public static Map<String, Map<String, BigDecimal>> makeFnlttSinglAcntAllMap(String stockId, String yq) {
        return makeFnlttSinglAcntAllMap(getFnlttSinglAcntAll(stockId, yq));
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

        Map<String, Map<String, BigDecimal>> map = makeFnlttSinglAcntAllMap("KOR_078340", "2023,2");

        Set<String> keys = map.keySet();
        for(String key : keys){

            Map<String, BigDecimal> dataMap = map.get(key);
            Set<String> dataKeys = dataMap.keySet();
            for(String dataKey : dataKeys){
                System.out.println(dataKey +", " + dataMap.get(dataKey).toPlainString() );
            }
        }

//        System.out.println(getFnlttSinglAcntAll("KOR_005930", "2024,2"));
    }
}
