package io.runon.stock.trading.country.kor;

import io.runon.commons.utils.time.YmdUtil;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.closed.days.ClosedDays;
import io.runon.trading.closed.days.ClosedDaysSet;

/**
 * 한국주식시장
 * @author macle
 */
public class KorStockMarket implements ClosedDays{

    private static class Singleton {
        private static final KorStockMarket instance = new KorStockMarket();
    }

    public static KorStockMarket getInstance(){
        return Singleton.instance;
    }

    private KorStockMarket(){
        closedDaysSet = new ClosedDaysSet();
        updateCloseDay();
    }

    private final ClosedDaysSet closedDaysSet;


    public String getLastTradeYmd(){
        return getLastTradeYmd(YmdUtil.now(TradingTimes.KOR_ZONE_ID));
    }

    public void updateCloseDay(){
        closedDaysSet.loadFile( ClosedDays.getCloseDaysFilePath(CountryCode.KOR));
    }

    @Override
    public boolean isClosedDay(String ymd){
        return closedDaysSet.isClosedDay(ymd);
    }

    /**
     * 최종거래일 얻기
     * @return 최종거래일
     */
    public String getLastTradeYmd(String ymd){
        return ClosedDays.getLastTradeYmd(closedDaysSet, ymd);
    }

    public String getMaxCloseDay(){
        return closedDaysSet.getMaxYmd();
    }

    public String getMinCloseDay(){
        return closedDaysSet.getMinYmd();
    }
}