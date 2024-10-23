package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.data.management.DailyLineGet;
import io.runon.trading.data.management.DailyLinesOut;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.nio.file.FileSystems;

/**
 * 한국 지수 일별 내리기
 * @author macle
 */
public class KorIndexDailyOut {
    private final DailyLinesOut dailyLinesOut = new DailyLinesOut();

    private final KoreainvestmentApi api = KoreainvestmentApi.getInstance();
    private final KoreainvestmentMarketApi marketApi = api.getMarketApi();

    public KorIndexDailyOut(){

        dailyLinesOut.setZoneId(TradingTimes.KOR_ZONE_ID);
        dailyLinesOut.setNextDay(100);
        dailyLinesOut.setLastLineCheck(true);
        dailyLinesOut.setSleepTime(api.getPeriodSleepTime());
        dailyLinesOut.setPathTimeLine(PathTimeLine.CSV);

    }

    public String [] getExchangeLines(String exchange, String beginYmd, String endYmd){
        TradeCandle[] candles = marketApi.getIndexCandles(exchange, beginYmd, endYmd);
        return CsvCandle.lines(candles);
    }

    public void out(){
        out("KOSPI");
        out("KOSDAQ");
    }

    /**
     * KOSPI , KOSDAQ
     * @param exchange 한국 거래소(KOSPI , KOSDAQ)
     */
    public void out(String exchange){


        final String exchangeFinal = exchange.toUpperCase();

        DailyLineGet get = (beginYmd, endYmd) -> getExchangeLines(exchangeFinal, beginYmd, endYmd);

        String fileSeparator = FileSystems.getDefault().getSeparator();

        String path = TradingConfig.getTradingDataPath() + fileSeparator +  "indices" + fileSeparator + "major" + fileSeparator +  "candle"  + fileSeparator + exchangeFinal + fileSeparator +"1d";

        dailyLinesOut.out(get, path);
    }

}
