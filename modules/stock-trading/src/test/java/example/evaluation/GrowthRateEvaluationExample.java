package example.evaluation;

import io.runon.commons.data.BigDecimalArray;
import io.runon.commons.utils.time.YmdUtils;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.candle.StockCandles;
import io.runon.trading.TradingTimes;
import io.runon.trading.evaluation.GrowthRateEvaluation;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * 벡테스팅 상에 자산 성장 평가를 위해 만들었지만 예제 편의삼 삼성전자 주가로 결과를 확인한다.
 *
 * @author macle
 */
public class GrowthRateEvaluationExample {
    public static void main(String[] args) {
        Stock stock = Stocks.getStock("KOR_078340");

        String endYmd  = YmdUtils.now(TradingTimes.KOR_ZONE_ID);
        String beginYmd = "20150101";

        TradeCandle[] candles = StockCandles.getDailyCandles(stock, "KRX",beginYmd, endYmd);


        BigDecimalArray evResult =   GrowthRateEvaluation.evaluation(candles, Stocks.getZoneId(stock));

        System.out.println(evResult);

        System.out.println(candles[0].getClose().toPlainString());
        System.out.println(candles[candles.length-1].getClose().toPlainString());
    }
}
