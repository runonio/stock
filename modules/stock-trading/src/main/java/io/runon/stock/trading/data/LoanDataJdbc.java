package io.runon.stock.trading.data;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockDailyData;
import io.runon.stock.trading.StockLoanDaily;
import io.runon.stock.trading.rds.RdsDaily;

import java.util.List;

/**
 * @author macle
 */
public class LoanDataJdbc  implements LoanData{

    @Override
    public StockLoanDaily[] getStockLoanDailies(Stock stock, String beginYmd, String endYmd) {

        List<StockDailyData> list =  RdsDaily.getDailyDataList("stock_loan", stock.getStockId(), beginYmd, endYmd);

        //대차잔고는 빠지는 데이터가 없어서 그대로 전송.
        return list.toArray(new StockLoanDaily[0]);
    }
}
