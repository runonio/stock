package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.management.StockOutTimeLineJson;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.data.daily.VolumePowerDaily;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.data.json.JsonOutLine;

/**
 * 일별 체결강도 정보 내리기(매수체결, 매도체결)
 * @author macle
 */
public class SpotDailyVolumePowerOut extends KoreainvestmentDailyOut{
    public SpotDailyVolumePowerOut() {
        super(StockPathLastTime.VOLUME_POWER, PathTimeLine.JSON);
        dailyOut.setServiceName("koreainvestment_program_trading");
    }

    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {
        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        KoreainvestmentPeriodDataApi periodDataApi = api.getPeriodDataApi();
        VolumePowerDaily[] dailies = periodDataApi.getVolumePowerDailies(stock.getSymbol(), beginYmd, endYmd);
        return JsonOutLine.getLines(dailies);
    }

    @Override
    public int getNextDay() {
        return 100;
    }

    @Override
    public String getDeletedPropertiesKey() {

        return "delisted_volume_power_1d";
    }
}
