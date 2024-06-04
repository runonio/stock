package io.runon.stock.trading.rds;

import com.seomse.jdbc.objects.JdbcObjects;
import io.runon.stock.trading.ShortSellingDaily;
import io.runon.stock.trading.StockDailyData;
import io.runon.stock.trading.StockLoanDaily;

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
        stockDailyData.setDataKey("stock_loan");
        stockDailyData.setDataValue(loanDaily.toString());
        stockDailyData.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(stockDailyData, false);

    }

    public static void saveShortSelling(String stockId, ShortSellingDaily shortSellingDaily){
        StockDailyData stockDailyData = new StockDailyData();
        stockDailyData.setStockId(stockId);
        stockDailyData.setYmd(shortSellingDaily.getYmd());
        stockDailyData.setDataKey("short_selling");
        stockDailyData.setDataValue(shortSellingDaily.toString());
        stockDailyData.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(stockDailyData, false);

    }

    public static List<StockDailyData> getDailyDataList(String dataKey, String stockId, String beginYmd, String endYmd){

        String where = "stock_id='" + stockId +"' and data_key='" + dataKey +"' and ymd >= " + beginYmd +" and ymd <= " +endYmd;
        return  JdbcObjects.getObjList(StockDailyData.class, where,"ymd asc");
    }

}
