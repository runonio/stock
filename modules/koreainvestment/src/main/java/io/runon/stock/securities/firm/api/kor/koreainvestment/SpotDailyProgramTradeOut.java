package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.daily.ProgramDaily;
import io.runon.stock.trading.data.management.StockOutTimeLineJson;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.data.file.PathTimeLine;

/**
 * 일별 프로그램 매매동향 내리기
 * @author macle
 */
public class SpotDailyProgramTradeOut extends KoreainvestmentDailyOut{
    public SpotDailyProgramTradeOut(){
        super(StockPathLastTime.PROGRAM, PathTimeLine.JSON);
        dailyOut.setServiceName("koreainvestment_program_trading");
    }

    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentPeriodDataApi periodDataApi = api.getPeriodDataApi();
        ProgramDaily[] dailies = periodDataApi.getProgramDailies(stock.getSymbol(), beginYmd, endYmd);
        return StockOutTimeLineJson.getLines(stock,dailies);
    }

    @Override
    public int getNextDay() {
        return 30;
    }
    public String getDelistedPropertiesKey() {
        return "delisted_program_trade_1d";
    }
}
