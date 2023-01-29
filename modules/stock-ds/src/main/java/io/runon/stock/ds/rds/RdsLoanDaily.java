package io.runon.stock.ds.rds;

import com.seomse.jdbc.objects.JdbcObjects;
import io.runon.stock.trading.StockLoanDaily;

/**
 * RDS 에 저장된 StockLoanDaily
 * 일별 대차(대주 정보)
 * @author macle
 */
public class RdsLoanDaily {

    public static void save(String stockId, StockLoanDaily loanDaily){
        StockDailyData stockDailyData = new StockDailyData();
        stockDailyData.setStockId(stockId);
        stockDailyData.setYmd(loanDaily.getYmd());
        stockDailyData.setDataKey("stock_loan");
        stockDailyData.setDataValue(loanDaily.toString());
        stockDailyData.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(stockDailyData, false);

    }



}
