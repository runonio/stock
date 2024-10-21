package io.runon.stock.securities.firm.api.kor.koreainvestment.service;

import com.seomse.commons.service.Service;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.time.Times;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentFiles;
import lombok.extern.slf4j.Slf4j;
/**
 * @author macle
 */
@Slf4j
public class ThemeUpdateService  extends Service {

    public ThemeUpdateService(){
        setServiceId(this.getClass().getName());
        setDelayStartTime(Times.MINUTE_7);
        setSleepTime(Times.HOUR_6);
        setState(State.START);
    }

    @Override
    public void work() {
        try{
            KoreainvestmentFiles.updateDownloadThemeList();
        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }
    }
}