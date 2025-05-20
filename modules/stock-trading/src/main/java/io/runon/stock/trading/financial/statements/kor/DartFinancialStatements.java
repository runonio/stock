package io.runon.stock.trading.financial.statements.kor;

import io.runon.commons.config.Config;
import io.runon.commons.exception.InvalidParameterException;
import io.runon.commons.utils.string.Check;
import io.runon.commons.utils.time.Times;
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

    public static FinancialStatements [] getFinancialStatementsArray(String idOrSymbol, boolean isConsolidated ){

        List<FinancialStatements> list = new ArrayList<>();

        int beginYear = 2015;

        int endYear = Times.getYear(TradingTimes.KOR_ZONE_ID) + 1;
        int endQuarter = 5;

        for (int year = beginYear; year < endYear ; year++) {
            for (int quarter = 1; quarter <endQuarter ; quarter++) {
                FinancialStatements financialStatements =getFinancialStatements(idOrSymbol, year, quarter, isConsolidated);
                if (financialStatements == null) {
                    continue;
                }
                if(!financialStatements.isIn()){
                    continue;
                }
                list.add(financialStatements);
            }
        }


        return list.toArray(new FinancialStatements[0]);
    }



    public static FinancialStatements getFinancialStatements(String idOrSymbol, int year, int quarter, boolean isConsolidated ){
        return getFinancialStatements(idOrSymbol, Integer.toString(year),quarter, isConsolidated);
    }

    /**
     *
     * @param idOrSymbol 종목 아이디 또는 symbol
     * @param year 년도
     * @param quarter 분기 1,2,3,4
     * @param isConsolidated 연결제무제표 여부
     * @return 제무재표
     */
    public static FinancialStatements getFinancialStatements(String idOrSymbol, String year, int quarter, boolean isConsolidated ){



        Map<String, Map<String, BigDecimal>> map = makeFnlttSinglAcntAllMap(idOrSymbol, year, quarter, isConsolidated);
        if(map.isEmpty()){
            return null;
        }
        if(!(map.containsKey("손익계산서") || map.containsKey("포괄손익계산서")) ){
            return null;
        }


        FinancialStatements financialStatements = getFinancialStatements(map);

        if(financialStatements == null || !financialStatements.isIn()){
            return null;
        }

        if(financialStatements.getSales() == null && financialStatements.getOperatingProfit() != null){
            //적자일때 매출이 0원이라 매출이 안 찍힌 경우
            Map<String, BigDecimal> dataMap= map.get("포괄손익계산서");

            if(dataMap == null){
                dataMap= map.get("손익계산서");
            }

            if(dataMap != null){
                BigDecimal num = dataMap.get("판매비와관리비");
                if(num == null){
                    num = dataMap.get("영업비용");
                }
                if(num == null){
                    num = dataMap.get("판매관리비");
                }

                if(num != null && num.compareTo(BigDecimal.ZERO) > 0){
                    num = num.multiply(BigDecimals.DECIMAL_M_1);
                }

                if(num != null && num.compareTo(financialStatements.getOperatingProfit()) == 0) {
                    financialStatements.setSales(BigDecimal.ZERO);
                }
            }
        }

        if(financialStatements.getCapital() == null && financialStatements.getAssets() != null && financialStatements.getLiabilities() != null){
            financialStatements.setCapital(financialStatements.getAssets().subtract(financialStatements.getLiabilities()));
        }
        if(financialStatements.getAssets() == null && financialStatements.getCapital() != null && financialStatements.getLiabilities() != null){
            financialStatements.setAssets(financialStatements.getCapital().add(financialStatements.getLiabilities()));
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
                , "매출"
                , "영업수익"
                , "영업수익(매출액)"
                , "수익(매출액)"
                , "매출원가"
                , "매출(영업수익)"
                , "합계"
                , "매출총이익"
                , "보험판매매출수입수수료"
                ,"보험판매수입수수료"
                , "수익"
                , "수익합계"
                ,"매출액및지분법손익"
                ,"매출과지분법손익(영업수익)"


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
                , "영업손실"
                , "영업손익"
                , "영업손익(손실)"
                , "영업총손실"
                , "영업순손익"
                , "이익"
                , "영업활동으로부터의이익"
                , "영업손실(이익)"
                ,"반기영업이익"
                ,"분기영업이익"
                ,"당기영업이익"
                ,"기말영업이익"
                ,"매출총이익"
        };


        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static BigDecimal getNetProfit( Map<String, Map<String, BigDecimal>> map) {

        String [] mapKeys = {
                "손익계산서"
                , "포괄손익계산서"
                , "자본변동표"
        };

        String [] numKeys = {
                "분기순이익(손실)"
                , "분기순손익"
                , "분기순손익(손실)"
                , "당기순이익(손실)"
                , "반기순이익(손실)"
                , "당기순이익"
                , "반기순이익"
                , "분기순이익"
                , "연결총분기순이익"
                , "연결총분기순이익(손실)"
                , "당기순손익"
                , "당기순손익(손실)"
                , "연결당기순이익(손실)"
                , "연결당기순이익"
                , "반기순손익"
                , "반기순손익(손실)"
                , "분기손이익(손실)"
                , "분기순손실"
                , "반기순손실"
                , "당기순손실"
                , "당분기순이익"
                , "당분기순이익(손실)"
                , "당반기순이익"
                , "당반기순이익(손실)"
                , "분기순수익"
                , "분기순수익(손실)"
                , "당기순수익"
                , "당기순수익(손실)"
                , "당분기손순익"
                , "분기손이익"
                ,"포괄손익당기순이익"
                ,"포괄손익분기순이익"
                ,"포괄손익반기순이익"
                , "분기분이익"
                , "당기분이익"
                , "반기분이익"
                ,"기순이익"
                ,"포괄손익"
                ,"당순이익"
                ,"당기순손실(이익)"
                ,"당기손이익"
                ,"분기손이익"
                ,"반기손이익"
                ,"분기분손실"
                ,"당기분손실"
                ,"반기분손실"
                ,"기말분손실"
                ,"반기총포괄손익"
                ,"분기총포괄손익"
                ,"당기총포괄손익"
                ,"기말총포괄손익"
                ,"순이익"
                ,"분기(당기)순이익"
                , "분기말순이익"
                ,"당기의순이익"
                ,"분기의순이익"
                ,"반기의순이익"
                ,"기말의순이익"
                ,"반기(당기)순이익"
                ,"분기(당기)순이익"
                ,"기말(당기)순이익"
                ,"당분기순손실"

                ,"당반기순손실"
                ,"당기순익"
                ,"분기순익"
                ,"반기순익"


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
                , "자본과부채총계"
                ,"부채와자본총계"
                ,"자산"
                ,"부채와자본계"
                ,"총자산"
        };


        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static BigDecimal getLiabilities( Map<String, Map<String, BigDecimal>> map) {

        String [] mapKeys = {
                "재무상태표"
        };

        String [] numKeys = {
                "부채총계"
                ,"부채"
                ,"총부채"
        };

        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static BigDecimal getCapital( Map<String, Map<String, BigDecimal>> map) {

        String [] mapKeys = {
                "재무상태표"
        };

        String [] numKeys = {
                "자본총계"
                ,"자본"
                , "자본금"
                ,"기말자본잔액"
                ,"분기말잔액"
                ,"반기말잔액"
                ,"기말자본"
                ,"분기말자본"
                ,"반기말자본"
                ,"당기말자본"
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
                , "영업활동으로인한순현금흐름"
                , "영업활동순현금흐름"
                , "영업에서창출된현금흐름"
                , "영업으로인한현금흐름"
                , "영업활동으로부터창출된현금흐름"
                , "영업활동으로인현금흐름"
                ,"영업활동으로부터의순현금유출"
                ,"영업활동으로부터의순현금유입(유출)"
                ,"영업활동으로부터의순현금유입"
                ,"영업활동으로인한자산부채의변동"
                , "영업활동으로인한현금"
                ,"영업활동으로부터의현금흐름"
                ,"영업으로부터창출된현금흐름"
                ,"영업에서사용된현금"
                , "영업활동으로인한순현금흐름합계"
                ,"영업활동에서창출된현금흐름"
                ,"영업활동연금흐름"
                ,"영업에서창출된현금"
                ,"영업활동에서창출된현금"
                ,"영업으로창출된현금흐름"
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
                , "현금및예치금"
                , "현금및현금성자산합계"
                , "현금성자산"
                ,"기초의현금및현금성자산"
                ,"현금및상각후원가측정예치금"
        };

        return BigDecimals.getNumber(map, mapKeys, numKeys);
    }

    public static String getFnlttSinglAcntAll(String stockId, String param){
        return  JdbcQuery.getResultOne("select data_value from stock_api_data where stock_id='" + stockId+"' and api_url='opendart.fss.or.kr/api/fnlttSinglAcntAll.json' and api_param='" +param+"'" );
    }


    public static Map<String, Map<String, BigDecimal>> makeFnlttSinglAcntAllMap(String idOrSymbol, String year, int quarter, boolean isConsolidated ) {
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


        return makeFnlttSinglAcntAllMap(getFnlttSinglAcntAll(stockId, param));
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

            name = name.replace("VIII.","");
            name = name.replace("VII.","");
            name = name.replace("III.","");
            name = name.replace("II.","");
            name = name.replace("VI.","");
            name = name.replace("IV.","");
            name = name.replace("V.","");
            name = name.replace("I.","");

            name = name.replace("1.","");
            name = name.replace("2.","");
            name = name.replace("3.","");
            name = name.replace("4.","");
            name = name.replace("5.","");
            name = name.replace("6.","");
            name = name.replace("7.","");

            name = name.replace("I","");
            name = name.replace("V","");
            name = name.replace("l","");
            name = name.replace(".","");

            name = name.replace("연결","");

            char [] chars = name.toCharArray();
            for(char ch : chars){
                if(Check.isHangulText(ch) || ch == '(' || ch == ')'){
                    continue;
                }

                name = name.replace(Character.toString(ch), "");
            }
            name = name.replace("(반)","");
            name = name.replace("(분)","");

            name = name.replace("(분기)","");
            name = name.replace("(반기)","");


            name = name.replace("당기기","당기");
            name = name.replace("분기기","분기");
            name = name.replace("반기기","반기");
            name = name.replace("순이이익","순이익");


            if(name.endsWith("(손실)") || name.endsWith("(수익)") || name.endsWith("(감소)")
            ||name.endsWith("(주석)")
            ){
                name =name.substring(0, name.lastIndexOf("("));
            }

            name = name.replace(" ","").trim();
            name = name.trim();

            if(name.startsWith("계속영업") && name.length() > 4){
                name = name.substring("계속영업".length());
            }

            if(name.startsWith("영업이익(")){
                name = "영업이익";
            }

            if(name.startsWith("총분기") || name.startsWith("총포괄")
            || name.startsWith("총당기") || name.startsWith("총반기") || name.startsWith("총기말")

            ) {
                name = name.substring(1);
            }

            if(name.endsWith("(매출액)")){
                name ="매출액";
            }
            if(name.endsWith("영업수익(매출)")){
                name ="매출액";
            }

            if(name.startsWith("매출액(")){
                name = "매출액";
            }

            if(name.startsWith("수익(")) {
                name ="수익";
            }

            if(name.startsWith("분기순이익(")){
                name = "분기순이익";
            }

            if(name.startsWith("분기순손실(")){
                name = "분기순손실";
            }

            if(name.startsWith("당기순이익(")){
                name = "당기순이익";
            }

            if(name.startsWith("반기순이익(")){
                name = "반기순이익";
            }

            if(name.endsWith("(영업수익)")){
                name = "매출액";
            }

            name = name.replace("순이익손실", "순이익");

            name =name.replace("(주)","");
            name =name.replace("()","");
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
        int beginYear = 2016;

        int maxYm = Integer.parseInt(YmdUtil.getYmd(YmdUtil.now(TradingTimes.KOR_ZONE_ID), -93).substring(0,6));

        String [] markets = {
                "KOSPI"
                , "KOSDAQ"
        };
        String [] types ={
                "STOCK"
        };


        Set<String> passKey = new HashSet<>();
        passKey.add("073540,2019,4,true");
        passKey.add("303030,2019,2,false");
        passKey.add("006200,2016,4,false");
        passKey.add("006200,2017,1,false");
        passKey.add("006200,2017,2,false");
        passKey.add("006200,2017,3,false");
        passKey.add("006200,2017,4,false");
        passKey.add("083470,2019,3,false");
        passKey.add("218150,2017,2,false");
        passKey.add("251630,2017,3,true");
        passKey.add("261200,2019,4,false");
        passKey.add("251630,2017,3,false");
        passKey.add("003550,2016,1,false");
        passKey.add("003550,2016,2,false");
        passKey.add("003550,2016,3,false");
        passKey.add("003550,2016,4,false");
        passKey.add("462980,2024,4,true");
        passKey.add("006840,2021,4,false");
        passKey.add("230240,2017,2,false");
        passKey.add("230240,2017,3,false");
        passKey.add("001430,2024,1,false");
        passKey.add("005990,2017,1,false");
        passKey.add("000480,2023,2,false");



        Set<String> passSymbol = new HashSet<>();
        passSymbol.add("261200");
        passSymbol.add("003550");
        passSymbol.add("000210");
        passSymbol.add("003690");
        passSymbol.add("019590");
        passSymbol.add("299660");
        passSymbol.add("194480");


        Stock [] stocks = Stocks.getAllStocks(markets, types);

        int cnt = 0;

        for(Stock stock : stocks){
//            System.out.println(stock.getStockId() +" "+ cnt++ +", " + stocks.length);

            if(passSymbol.contains(stock.getSymbol())){
                continue;
            }

            outer:
            for(int year = beginYear;; year++){

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

                    String checkKey = stock.getSymbol()+","+year+","+quarter+",true";



                    FinancialStatements financialStatements =  getFinancialStatements(stock.getStockId(), year,quarter,true);
                    Map<String, Map<String, BigDecimal>> map = makeFnlttSinglAcntAllMap(stock.getStockId(), Integer.toString(year),quarter,true);


                    if(!passKey.contains(checkKey) && financialStatements != null && financialStatements.isNullIn()){

                        if(financialStatements.getOperatingCashFlow() == null){
                            financialStatements.setOperatingCashFlow(new BigDecimal(0));
                            if(!financialStatements.isNullIn()){

                                Map<String, BigDecimal> dataMap = map.get("현금흐름표");
                                if(dataMap == null){
                                    continue ;
                                }
                                Set<String> keys = dataMap.keySet();

                                boolean isIn = false;
                                for(String dataKey : keys){
                                    if(dataKey.contains("영업") && !dataKey.contains("이자")&& !dataKey.contains("수취")&& !dataKey.contains("배당금")){
                                        isIn = true;
                                        break;
                                    }
                                }
                                if(!isIn){
                                    continue ;
                                }

                            }

                            financialStatements.setOperatingCashFlow(null);
                        }

                        if(stock.getNameKo().endsWith("생명") || stock.getNameKo().endsWith("보험") || stock.getNameKo().endsWith("캐피탈")
                                || stock.getNameKo().contains("금융")     || stock.getNameKo().contains("증권")  || stock.getNameKo().contains("은행")
                                || stock.getNameKo().endsWith("지주")     || stock.getNameKo().endsWith("투자") || stock.getNameKo().endsWith("화재")
                                || stock.getNameKo().endsWith("해상")
                        ){
                            if(financialStatements.getSales() == null){
                                financialStatements.setSales(new BigDecimal(0));
                                if(!financialStatements.isNullIn()){
                                    continue ;
                                }
                                financialStatements.setSales(null);
                            }
                        }

                        Map<String, BigDecimal> dataMap =  map.get("포괄손익계산서");
                        if(dataMap == null){
                            dataMap = map.get("손익계산서");
                        }
                        if(dataMap.size() < 3){
                            continue ;
                        }
                        if(financialStatements.getCashEquivalents() == null){
                            financialStatements.setCashEquivalents(new BigDecimal(0));
                            if(!financialStatements.isNullIn()){
                                continue ;
                            }
                        }
//                        consoleView(stock.getStockId(), Integer.toString(year),quarter,true);
//                        System.out.println(financialStatements);
                        System.out.println(stock.getSymbol() +"," + year +"," + quarter +",true" );
//                        System.out.println(stock.getNameKo());
//                        return;
                    }


                    map = makeFnlttSinglAcntAllMap(stock.getStockId(), Integer.toString(year),quarter,false);

                    checkKey = stock.getSymbol()+","+year+","+quarter+",false";
                    financialStatements =  getFinancialStatements(stock.getStockId(), year,quarter,false);
                    if(!passKey.contains(checkKey) &&financialStatements != null && financialStatements.isNullIn()){

                        if(financialStatements.getOperatingCashFlow() == null){
                            financialStatements.setOperatingCashFlow(new BigDecimal(0));
                            if(!financialStatements.isNullIn()){
                                Map<String, BigDecimal> dataMap = map.get("현금흐름표");
                                if(dataMap == null){
                                    continue ;
                                }
                                Set<String> keys = dataMap.keySet();

                                boolean isIn = false;
                                for(String dataKey : keys){
                                    if(dataKey.contains("영업") && !dataKey.contains("이자")
                                            && !dataKey.contains("수취")
                                            && !dataKey.contains("배당금")){
                                        System.out.println(dataKey);

                                        isIn = true;
                                        break;
                                    }
                                }
                                if(!isIn){
                                    continue ;
                                }
                            }

                            financialStatements.setOperatingCashFlow(null);
                        }

                        if(stock.getNameKo().endsWith("생명") || stock.getNameKo().endsWith("보험") || stock.getNameKo().endsWith("캐피탈")
                         || stock.getNameKo().contains("금융") || stock.getNameKo().contains("증권")  || stock.getNameKo().contains("은행")
                                || stock.getNameKo().endsWith("지주")   || stock.getNameKo().endsWith("투자") || stock.getNameKo().endsWith("화재")
                                || stock.getNameKo().endsWith("해상")
                        ){
                            if(financialStatements.getSales() == null){
                                financialStatements.setSales(new BigDecimal(0));
                                if(!financialStatements.isNullIn()){
                                    continue ;
                                }
                                financialStatements.setSales(null);
                            }
                        }

                        Map<String, BigDecimal> dataMap =  map.get("포괄손익계산서");
                        if(dataMap == null){
                            dataMap = map.get("손익계산서");
                        }
                        if(dataMap.size() < 3){
                            continue ;
                        }

                        if(financialStatements.getCashEquivalents() == null){
                            financialStatements.setCashEquivalents(new BigDecimal(0));
                            if(!financialStatements.isNullIn()){
                                continue ;
                            }
                        }

//                        consoleView(stock.getStockId(), Integer.toString(year),quarter,false);
//                        System.out.println(financialStatements);
                        System.out.println(stock.getSymbol() +"," + year +"," + quarter +",false");
//                        System.out.println(stock.getNameKo());
//                        return;
                    }

                }
            }
        }
    }

    public static void main(String[] args) {
        Config.getConfig("");

        searchNullIn();

//        OFS:재무제표(별도), CFS:연결재무제표
//
//        String symbol = "006120";
//        String year = "2024";
//        int quarter = 4;
//        boolean isConsolidated = false;
//
//        consoleView(symbol,year,quarter,isConsolidated);
//
//        FinancialStatements financialStatements =  getFinancialStatements(symbol,year,quarter,isConsolidated);
//        System.out.println(financialStatements);
    }
}
