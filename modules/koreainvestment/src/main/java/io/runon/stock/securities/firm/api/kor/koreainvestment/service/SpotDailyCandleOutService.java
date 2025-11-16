package io.runon.stock.securities.firm.api.kor.koreainvestment.service;

import io.runon.commons.config.Config;
import io.runon.commons.service.Service;
import io.runon.commons.utils.ExceptionUtils;
import io.runon.commons.utils.time.Times;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApis;
import lombok.extern.slf4j.Slf4j;

/**
 * @author macle
 */
@Slf4j
public class SpotDailyCandleOutService extends Service {
    public SpotDailyCandleOutService(){
        setSleepTime(Config.getLong("stock.daily.candle.out.service.sleep", Times.HOUR_12));
        setState(Service.State.START);
    }

    @Override
    public void work() {
        try{
            KoreainvestmentApis.korStocksDailyCandleOut();
        }catch (Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

}
