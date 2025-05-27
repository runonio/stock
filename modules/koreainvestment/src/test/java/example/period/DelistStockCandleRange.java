package example.period;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.candle.StockCandles;
import io.runon.trading.data.DataPathTimeRange;

/**
 * @author macle
 */
public class DelistStockCandleRange {
    public static void main(String[] args) {
        String stockId = "KOR_182690";

        DataPathTimeRange range = StockCandles.getSpotCandleTimeRange(stockId,null, "1d");

        Stock stock = Stocks.getStock(stockId);

        System.out.println(stock);

        System.out.println(range.getBeginYmdText() +", " + range.getEndYmdText());

    }
}
