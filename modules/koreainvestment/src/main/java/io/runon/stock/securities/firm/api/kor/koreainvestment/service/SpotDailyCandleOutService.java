package io.runon.stock.securities.firm.api.kor.koreainvestment.service;

import com.seomse.commons.config.Config;
import com.seomse.commons.service.Service;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.time.Times;
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
            log.error(ExceptionUtil.getStackTrace(e));
        }
    }

}
