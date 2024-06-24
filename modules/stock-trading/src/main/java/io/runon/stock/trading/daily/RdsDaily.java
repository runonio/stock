package io.runon.stock.trading.daily;

import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.objects.JdbcObjects;
import io.runon.stock.trading.StockDailyData;
import io.runon.trading.data.DailyData;

import java.util.List;

/**
 * RDS 에 저장된 일별
 * 일별 대차(대주 정보)
 * @author macle
 */
public class RdsDaily {

    public static void saveLoan(String stockId, StockLoanDaily loanDaily){
        StockDailyData stockDailyData = new StockDailyData();
        stockDailyData.setStockId(stockId);
        stockDailyData.setYmd(loanDaily.getYmd());
        stockDailyData.setDataKey(RdsDataKey.STOCK_LOAN);
        stockDailyData.setDataValue(loanDaily.toString());
        stockDailyData.setUpdatedAt(System.currentTimeMillis());


        JdbcObjects.insertOrUpdate(stockDailyData, false);

    }

    public static void saveShortSelling(String stockId, ShortSellingDaily shortSellingDaily){
        StockDailyData stockDailyData = new StockDailyData();
        stockDailyData.setStockId(stockId);
        stockDailyData.setYmd(shortSellingDaily.getYmd());
        stockDailyData.setDataKey(RdsDataKey.SHORT_SELLING);
        stockDailyData.setDataValue(shortSellingDaily.toString());
        stockDailyData.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(stockDailyData, false);

    }

    public static String getDailyData(String key, int ymd){
        return JdbcQuery.getResultOne("select data_value from daily_data where data_key='" + key +"' and ymd=" + ymd);
    }

//
//    public static void saveKorMarketCreditLoan(KorMarketCreditLoanDaily daily){
//        final String dataKey ="kor_market_credit_loan";
//
//    }


    public static void save(String dataKey, int ymd, String dataValue){
        String lastDataValue = RdsDaily.getDailyData(dataKey, ymd);

        if(lastDataValue == null){
            //인서트
            DailyData dailyData = makeDailyData(dataKey, ymd, dataValue);
            JdbcObjects.insert(dailyData);
        }else{
            //기존 데이터 비교
            if(lastDataValue.equals(dataValue)){
                //변한게 없으면
                return;
            }

            DailyData dailyData = makeDailyData(dataKey, ymd, dataValue);
            JdbcObjects.update(dailyData);
        }
    }

    public static DailyData makeDailyData(String dataKey, int ymd, String dataValue){
        DailyData dailyData = new DailyData();
        dailyData.setDataKey(dataKey);
        dailyData.setYmd(ymd);
        dailyData.setDataValue(dataValue);
        dailyData.setUpdatedAt(System.currentTimeMillis());

        return dailyData;
    }


    public static List<StockDailyData> getDailyDataList(String dataKey, String stockId, String beginYmd, String endYmd){

        String where = "stock_id='" + stockId +"' and data_key='" + dataKey +"' and ymd >= " + beginYmd +" and ymd <= " +endYmd;
        return  JdbcObjects.getObjList(StockDailyData.class, where,"ymd asc");
    }

}
