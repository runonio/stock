package io.runon.stock.trading.daily;

import io.runon.commons.exception.ReflectiveOperationRuntimeException;
import io.runon.commons.utils.DataCheck;
import io.runon.jdbc.JdbcQuery;
import io.runon.jdbc.objects.JdbcObjects;
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
        update(stockDailyData);

    }

    public static void saveShortSelling(String stockId, ShortSellingDaily shortSellingDaily){
        StockDailyData stockDailyData = new StockDailyData();
        stockDailyData.setStockId(stockId);
        stockDailyData.setYmd(shortSellingDaily.getYmd());
        stockDailyData.setDataKey(RdsDataKey.SHORT_SELLING);
        stockDailyData.setDataValue(shortSellingDaily.toString());
        update(stockDailyData);
    }

    public static String getDailyData(String key, int ymd){
        return JdbcQuery.getResultOne("select data_value from daily_data where data_key='" + key +"' and ymd=" + ymd);
    }


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

    public static boolean update(StockDailyData dailyData){
        try {
            String where = JdbcObjects.getCheckWhere(dailyData);
            StockDailyData lastData = JdbcObjects.getObj(StockDailyData.class, where);
            if(lastData == null){
                JdbcObjects.insert(dailyData);
                return true;
            }

            if(DataCheck.isEqualsObj(lastData.getDataValue(), dailyData.getDataValue())){
                return false;
            }

            dailyData.setUpdatedAt(System.currentTimeMillis());
            JdbcObjects.update(dailyData, true);
            return true;

        }catch (ReflectiveOperationException e){
            throw new ReflectiveOperationRuntimeException(e);
        }
    }

}
