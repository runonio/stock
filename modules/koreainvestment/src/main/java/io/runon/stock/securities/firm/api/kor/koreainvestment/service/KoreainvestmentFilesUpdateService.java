package io.runon.stock.securities.firm.api.kor.koreainvestment.service;

import io.runon.commons.service.Service;
import io.runon.commons.utils.ExceptionUtil;
import io.runon.commons.utils.time.Times;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentFiles;
import lombok.extern.slf4j.Slf4j;
/**
 * @author macle
 */
@Slf4j
public class KoreainvestmentFilesUpdateService extends Service {

    public KoreainvestmentFilesUpdateService(){
        setServiceId(this.getClass().getName());
        setDelayStartTime(Times.MINUTE_7);
        setSleepTime(Times.HOUR_6);
        setState(State.START);
    }

    @Override
    public void work() {
        try{
            KoreainvestmentFiles.updateDownloadThemeList();
            KoreainvestmentFiles.updateOverseasStocks();
        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }
    }

    public static void main(String[] args) {
        KoreainvestmentFiles.updateOverseasStocks();
    }
}