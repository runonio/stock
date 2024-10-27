package io.runon.stock.securities.firm.api.kor.koreainvestment.service;

import com.seomse.commons.service.Service;
import com.seomse.commons.utils.ExceptionUtil;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.trading.country.kor.KorStockMarket;
import lombok.extern.slf4j.Slf4j;

/**
 * @author macle
 */
@Slf4j
public class ClosedDaysFileOutService extends Service {

    public ClosedDaysFileOutService(){

    }

    @Override
    public void work() {
        try{
            boolean isChange = KoreainvestmentApi.getInstance().closedDaysOut();
            if(isChange){
                KorStockMarket.getInstance().updateCloseDay();
            }
        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }
    }
}
