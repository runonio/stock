package io.runon.stock.trading.data;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockDailyData;
import io.runon.stock.trading.data.daily.RdsDaily;
import io.runon.stock.trading.data.daily.ShortSellingDaily;
import io.runon.stock.trading.data.daily.StockLoanDaily;


import java.util.ArrayList;
import java.util.List;

/**
 * @author macle
 */
public class LoanDataJdbc  implements LoanData{

    @Override
    public StockLoanDaily[] getStockLoanDailies(Stock stock, String beginYmd, String endYmd) {

        List<StockDailyData> list =  RdsDaily.getDailyDataList("stock_loan", stock.getStockId(), beginYmd, endYmd);

        StockLoanDaily [] dailies = new StockLoanDaily[list.size()];
        for (int i = 0; i <dailies.length ; i++) {
            StockLoanDaily daily = StockLoanDaily.make(list.get(i).getDataValue(), stock);
            dailies[i] = daily;
        }

        //대차잔고는 빠지는 데이터가 없어서 그대로 전송.
        return dailies;
    }

    @Override
    public ShortSellingDaily[] getShortSellingDailies(Stock stock, String beginYmd, String endYmd) {
        List<StockDailyData> dataList =  RdsDaily.getDailyDataList("short_selling", stock.getStockId(), beginYmd, endYmd);

        List<ShortSellingDaily> dailyList = new ArrayList<>();

        for(StockDailyData data : dataList){
            ShortSellingDaily shortSellingDaily = ShortSellingDaily.make(data.getDataValue(), stock);
            if(shortSellingDaily.getBalanceQty() == null){
                continue;
            }
            dailyList.add(shortSellingDaily);
        }

        dataList.clear();

        ShortSellingDaily [] dailies = dailyList.toArray(new ShortSellingDaily[0]);
        dailyList.clear();
        return dailies;
    }
}
