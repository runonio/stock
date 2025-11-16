package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.commons.config.JsonFileProperties;
import io.runon.commons.utils.time.YmdUtils;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.management.OverseasSpotDailyOut;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * 한국 투자증권 해외주식 기간별 시세 관련 api 정리
 * @author macle
 */
public class UasSpotDailyCandleOut extends OverseasSpotDailyOut {

    protected final KoreainvestmentApi koreainvestmentApi = KoreainvestmentApi.getInstance();
    protected final KoreainvestmentOverseasPeriodApi overseasPeriodApi = koreainvestmentApi.getOverseasPeriodApi();

    public UasSpotDailyCandleOut() {
        super(CountryCode.USA, StockPathLastTime.CANDLE, PathTimeLine.CSV);
        dailyOut.setListedNullBeginYmd("19900101");
    }

    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {
        String jsonText = overseasPeriodApi.getDailyJsonText(stock.getExchange(), stock.getSymbol(), endYmd, true);
        TradeCandle [] candles = overseasPeriodApi.getDailyCandles(CountryCode.USA, jsonText);
        int beginIndex = 0;
        for (int i = 0; i <candles.length ; i++) {
            String ymd = YmdUtils.getYmd(candles[i].getOpenTime(), TradingTimes.USA_ZONE_ID);
            if(YmdUtils.compare(ymd, beginYmd)>=0){
                beginIndex = i;
                break;
            }
        }

        if(beginIndex == 0){
            return CsvCandle.lines(candles);
        }

        String [] lines = new String[candles.length - beginIndex];
        for (int i = 0; i <lines.length ; i++) {
            lines[i] = CsvCandle.value(candles[beginIndex++]);
        }

        return lines;
    }

    @Override
    public void sleep() {
        koreainvestmentApi.periodSleep();
    }

    @Override
    public int getNextDay() {
        return 90;
    }

    @Override
    public JsonFileProperties getJsonFileProperties() {
        return koreainvestmentApi.getJsonFileProperties();
    }

    @Override
    public String getDelistedPropertiesKey() {
        return "uas_delisted_stocks_candle_1d";
    }
}
