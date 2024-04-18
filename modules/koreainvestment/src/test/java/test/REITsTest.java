package test;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentPeriodDataApi;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * REITs 로표시되는종목이 ETF, ETN, 주식중 어떤걸로 분별되는지 테스트하기
 * @author macle
 */
public class REITsTest {
    public static void main(String[] args) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();

        KoreainvestmentPeriodDataApi periodDataApi = api.getPeriodDataApi();

        String text = periodDataApi.getPeriodDataJsonText("350520","D","20240301","20240401",true);

        TradeCandle[] candles = KoreainvestmentPeriodDataApi.getCandles(text);

        System.out.println(candles[0]);

    }
}
