package example;

import io.runon.commons.config.Config;
import io.runon.stock.trading.StockDailyData;
import io.runon.stock.trading.data.daily.RdsDaily;


import java.util.List;

/**
 * @author macle
 */
public class JdbcDailyDataExample {
    public static void main(String[] args) {
        Config.getConfig("");
        List<StockDailyData> dailyDatalist = RdsDaily.getDailyDataList("stock_loan","KOR_005930","20240501","20240528");
        for(StockDailyData dailyData : dailyDatalist){
            System.out.println(dailyData.getDataValue());
        }

        System.out.println(dailyDatalist.size());
    }
}
