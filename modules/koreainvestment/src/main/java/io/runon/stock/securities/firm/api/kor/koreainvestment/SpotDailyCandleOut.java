package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.extern.slf4j.Slf4j;

/**
 * 현물 일봉 캔들 내리기
 * 한국 투자증권은 아직 분봉과거치를 지원하지 않음
 * 올해 지원예정 중이라고 하였음
 * @author macle
 */
@Slf4j
public class SpotDailyCandleOut extends KoreainvestmentDailyOut{

    public SpotDailyCandleOut(){
        super(StockPathLastTime.CANDLE, PathTimeLine.CSV);
        dailyOut.setServiceName("koreainvestment_candle");
    }

    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {
        String text = periodDataApi.getPeriodDataJsonText(stock.getSymbol(),"J","D", beginYmd, endYmd, true);
        TradeCandle [] candles = KoreainvestmentPeriodDataApi.getCandles(text);
        return CsvCandle.lines(candles);
    }

    @Override
    public int getNextDay() {
        return 100;
    }




    public String getDelistedPropertiesKey() {
        return "delisted_stocks_candle_1d";
    }
}
