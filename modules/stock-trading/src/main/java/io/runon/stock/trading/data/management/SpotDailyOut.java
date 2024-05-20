package io.runon.stock.trading.data.management;

import com.seomse.commons.config.JsonFileProperties;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.FileLineOut;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.data.file.TimeLines;
import io.runon.trading.data.file.TimeName;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;


/**
 * 현물 일봉 캔들 내리기
 * 한국 투자증권은 아직 분봉과거치를 지원하지 않음
 * 올해 지원예정 중이라고 하였음
 * @author macle
 */
@Slf4j
public class SpotDailyOut {
    protected final StockDailyOutParam param;
    protected final StockPathLastTime stockPathLastTime;


    public SpotDailyOut(StockDailyOutParam param){

        this.param = param;
        stockPathLastTime = param.getStockPathLastTime();
    }


    private ZoneId zoneId = TradingConfig.DEFAULT_TIME_ZONE_ID;

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }


    public void out(){
        //전체 종목 일봉 내리기
        //KONEX 는 제외
        String [] exchanges = param.getExchanges();

        Stock [] stocks = Stocks.getStocks(exchanges);

        Stocks.sortUseLastTimeParallel(stocks,"1d", stockPathLastTime);

        for(Stock stock : stocks){
            try {
                //같은 데이터를 호출하면 호출 제한이 걸리는 경우가 있다 전체 캔들을 내릴때는 예외처리를 강제해서 멈추지 않는 로직을 추가
                out(stock);
            }catch (Exception e){
                try{
                    Thread.sleep(5000L);
                }catch (Exception ignore){}
                log.error(ExceptionUtil.getStackTrace(e) +"\n" + stock);
            }
        }
    }

    //상폐된 주식 캔들 내리기
    public void outDelisted(){
        String [] exchanges = param.getExchanges();

        JsonFileProperties jsonFileProperties = param.getJsonFileProperties();

        String delistedYmd = jsonFileProperties.getString(param.getDeletedPropertiesKey(),"19900101");

        String nowYmd = YmdUtil.now(TradingTimes.KOR_ZONE_ID);

        Stock [] stocks = Stocks.getDelistedStocks(exchanges, delistedYmd, nowYmd);
        for(Stock stock : stocks){
            try {
                //같은 데이터를 호출하면 호출 제한이 걸리는 경우가 있다 전체 캔들을 내릴때는 예외처리를 강제해서 멈추지 않는 로직을 추가
                out(stock);
            }catch (Exception e){
                try{
                    Thread.sleep(5000L);
                }catch (Exception ignore){}
                log.error(ExceptionUtil.getStackTrace(e) +"\n" + stock);
            }
        }

        jsonFileProperties.set(param.getDeletedPropertiesKey(), nowYmd);

    }

    /**
     * 상장 시점부터 내릴 수 있는 전체 정보를 내린다.
     * @param stock 종목정보
     */
    public void out(Stock stock){


        String nowYmd = YmdUtil.now(TradingTimes.KOR_ZONE_ID);
        int nowYmdNum = Integer.parseInt(nowYmd);

        //초기 데이터는 상장 년원일
        String nextYmd ;

        String filesDirPath = StockPaths.getSpotCandleFilesPath(stock.getStockId(),"1d");

        PathTimeLine pathTimeLine = param.getPathTimeLine();

        long lastTime = pathTimeLine.getLastTime(filesDirPath);

        if(lastTime > -1){
            nextYmd = YmdUtil.getYmd(lastTime, TradingTimes.KOR_ZONE_ID);

            if(stock.getDelistedYmd() != null){
                int lastYmdInt = Integer.parseInt(nextYmd);
                if(lastYmdInt >= stock.getDelistedYmd()){
                    //상폐종목인경우 이미 캔들이 다 저장되어 있을때
                    return ;
                }
            }

        }else{
            if(stock.getListedYmd() == null){
                log.error("listed ymd null: " + stock);
                return ;
            }
            nextYmd = Integer.toString(stock.getListedYmd());

        }

        TimeName.Type timeNameType = TimeName.getCandleType(Times.DAY_1);

        boolean isFirst = true;

        log.debug("start stock: " + stock);

        int maxYmd = nowYmdNum;

        if(stock.getDelistedYmd() != null){
            maxYmd = stock.getDelistedYmd();
        }


        //최대100건
        for(;;){

            if(YmdUtil.compare(nextYmd, nowYmd) > 0){
                break;
            }

            String endYmd = YmdUtil.getYmd(nextYmd, param.getNextDay());

            int endYmdNum =  Integer.parseInt(endYmd);
            if(endYmdNum > maxYmd){
                endYmd = Integer.toString(maxYmd);
            }

            String [] lines = param.getLines(stock, nextYmd, endYmd);

            if(isFirst) {

                FileLineOut.outBackPartChange(pathTimeLine, lines, filesDirPath, timeNameType, TradingTimes.KOR_ZONE_ID);
                isFirst = false;
            }else{
                FileLineOut.outNewLines(pathTimeLine, lines, filesDirPath, timeNameType, TradingTimes.KOR_ZONE_ID);
            }

            if(endYmdNum >= maxYmd){
                break;
            }

            if(lines.length == 0){
                nextYmd = YmdUtil.getYmd(endYmd, 1);
            }else{
                nextYmd = YmdUtil.getYmd(TimeLines.getMaxYmd(param.getPathTimeLine(), lines, zoneId),1);
            }

            param.sleep();
        }
    }

}
