package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.extern.slf4j.Slf4j;

/**
 * 일별 개별 신용정보 내리기
 * @author macle
 */
@Slf4j
public class SpotDailyCreditLoanOut extends KoreainvestmentDailyOut{

    public SpotDailyCreditLoanOut(){
        super(StockPathLastTime.CREDIT_LOAN, PathTimeLine.JSON);
    }


    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {
        String text = periodDataApi.getPeriodDataJsonText(stock.getSymbol(),"D", beginYmd, endYmd, true);
        TradeCandle[] candles = KoreainvestmentPeriodDataApi.getCandles(text);
        return CsvCandle.lines(candles);
    }

    @Override
    public int getNextDay() {
        return 30;
    }

    public String getDeletedPropertiesKey() {
        return "delisted_stocks_candle_1d";
    }





}
