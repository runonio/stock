package dev;

import com.seomse.commons.utils.time.TimeUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.path.StockPathLastTimeCandleSpot;

/**
 * 주식 캔들 관련 유틸성 클래스
 * @author macle
 */
public class StockSortTest {
    public static void main(String[] args) {
        String [] exchanges = {
                "KOSPI"
                , "KOSDAQ"
        };

        Stock[] stocks = Stocks.getStocks(exchanges);

        System.out.println(stocks[0]);
        System.out.println(stocks[1]);

        long sortBeginTime = System.currentTimeMillis();
        Stocks.sortUseLastTimeParallel(stocks,"1d", new StockPathLastTimeCandleSpot());

        System.out.println(TimeUtil.getTimeValue(System.currentTimeMillis() - sortBeginTime));
        System.out.println("-------------------------");



        System.out.println(stocks[0]);
        System.out.println(stocks[1]);



    }
}
