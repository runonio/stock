package io.runon.stock.trading.data.management;

import io.runon.commons.config.JsonFileProperties;
import io.runon.commons.utils.ExceptionUtil;
import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockYmd;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.StockData;
import io.runon.stock.trading.data.StockDataManager;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingConfig;
import io.runon.trading.data.file.FileLineOut;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.data.file.TimeName;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;


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

    protected CountryCode countryCode = TradingConfig.getDefaultCountryCode();


    //장중에 수집하는경우 마지막 라인의 변화를 체크하는 옵션
    //공매도나 대차잔고등 일별집계가 끝나는 데이터는 falst로 설정
    protected boolean isLastLineCheck = true;

    protected String serviceName;

    protected boolean isDelisted = false;

    protected String listedNullBeginYmd= null;

    protected final String interval ="1d";

    public SpotDailyOut(StockDailyOutParam param){

        this.param = param;
        stockPathLastTime = param.getStockPathLastTime();
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }


    protected ZoneId zoneId = TradingConfig.DEFAULT_TIME_ZONE_ID;

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }
    private final Map<String, StockYmd> lastYmdMap = new HashMap<>();

    public void setLastLineCheck(boolean lastLineCheck) {
        isLastLineCheck = lastLineCheck;
    }

    public void setLastYmdMap(Stock [] stocks){
        SpotOuts.setLastYmdMap(lastYmdMap, stocks, countryCode,param.getStockPathLastTime(), interval, zoneId);
    }


    public void outLastYmdMap(){
        SpotOuts.outLastYmdMap(lastYmdMap, countryCode, param.getStockPathLastTime(), interval);
    }


    public void out(){
        //전체 종목 일봉 내리기
        //KONEX 는 제외
        String [] exchanges = param.getExchanges();
        Stock[] stocks;
        if(isDelisted) {
            StockDataManager stockDataManager = StockDataManager.getInstance();
            StockData stockData = stockDataManager.getStockData();
            stocks = stockData.getAllStocks(exchanges);
        }else{
            stocks = Stocks.getStocks(exchanges);
        }
        out(stocks);
    }

    public void out(Stock [] stocks){
        setLastYmdMap(stocks);

        Stocks.sortUseLastTimeParallel(stocks,interval, stockPathLastTime);
        int count = 0;

        for(Stock stock : stocks){
            try {
                //같은 데이터를 호출하면 호출 제한이 걸리는 경우가 있다 전체 캔들을 내릴때는 예외처리를 강제해서 멈추지 않는 로직을 추가
                out(stock);
                param.sleep();
                count++;
                if(count > 50){
                    outLastYmdMap();
                    count = 0;
                }
            }catch (Exception e){
                log.error(ExceptionUtil.getStackTrace(e) +"\n" + stock);
                try{
                    Thread.sleep(Times.MINUTE_1);
                }catch (Exception ignore){}
            }
        }

        outLastYmdMap();
    }

    //상폐된 주식 캔들 내리기
    public void outDelisted(){

        String [] exchanges = param.getExchanges();

        JsonFileProperties jsonFileProperties = param.getJsonFileProperties();

        String delistedYmd = jsonFileProperties.getString(param.getDeletedPropertiesKey(),"19900101");

        String nowYmd = YmdUtil.now(zoneId);

        Stock [] stocks = Stocks.getDelistedStocks(exchanges, delistedYmd, nowYmd);

        out(stocks);

        jsonFileProperties.set(param.getDeletedPropertiesKey(), nowYmd);

    }

    /**
     * 상장 시점부터 내릴 수 있는 전체 정보를 내린다.
     * @param stock 종목정보
     */
    public void out(Stock stock){

        String nowYmd = YmdUtil.now(zoneId);
        int nowYmdNum = Integer.parseInt(nowYmd);

        //초기 데이터는 상장 년원일
        String nextYmd ;

        String filesDirPath = stockPathLastTime.getFilesDirPath(stock, interval);

        File filesDirFile = new File(filesDirPath);
        //noinspection ResultOfMethodCallIgnored
        filesDirFile.mkdirs();

        PathTimeLine pathTimeLine = param.getPathTimeLine();

        long lastTime = pathTimeLine.getLastTime(filesDirPath);

        if(lastTime < 0){
            //타임파일이 라인이 없으면 매핑정보
            //데이터가 없는데 계속 호출하는 로직방지
            StockYmd stockYmd = lastYmdMap.get(stock.getStockId());
            if(stockYmd != null){
                lastTime = Stocks.getDailyOpenTime(stockYmd.getStock(), stockYmd.getYmd());
            }
        }

        if(lastTime > -1){

            nextYmd = YmdUtil.getYmd(lastTime, zoneId);
            if(!isLastLineCheck){
                // 마지막일자를 체크하지 않으면 마지막 저장 날짜의 다음날짜를 호출한다.
                nextYmd = YmdUtil.getYmd(nextYmd, 1);
            }

            if(stock.getDelistedYmd() != null){
                int lastYmdInt = Integer.parseInt(nextYmd);
                if(lastYmdInt >= stock.getDelistedYmd()){
                    //상폐종목인경우 이미 캔들이 다 저장되어 있을때
                    return ;
                }
            }
        }else{
            if(stock.getListedYmd() == null){
                if(listedNullBeginYmd == null) {
                    log.error("listed ymd null: " + stock);
                    return;
                }else{
                    nextYmd = listedNullBeginYmd;
                }
            }else{
                nextYmd = Integer.toString(stock.getListedYmd());
            }
        }

        TimeName.Type timeNameType = TimeName.getDefaultType(Times.DAY_1);

        boolean isFirst = true;

        if(serviceName == null) {
            log.debug("start stock: " + stock);
        }else{
            log.debug(serviceName + " start stock: " + stock);
        }
        int maxYmd = nowYmdNum;

        if(stock.getDelistedYmd() != null){
            maxYmd = stock.getDelistedYmd();
        }

        int callCount = 0;
        for(;;){

            if (YmdUtil.compare(nextYmd, nowYmd) > 0) {
                break;
            }

            int nextYmdNum = Integer.parseInt(nextYmd);
            if(nextYmdNum > maxYmd){
                break;
            }

            String endYmd = YmdUtil.getYmd(nextYmd, param.getNextDay());
            callCount++;
            int endYmdNum =  Integer.parseInt(endYmd);
            if(endYmdNum > maxYmd){
                endYmd = Integer.toString(maxYmd);
            }

            String [] lines = param.getLines(stock, nextYmd, endYmd);
            param.sleep();

            if(isLastLineCheck && isFirst) {

                FileLineOut.outBackPartChange(pathTimeLine, lines, filesDirPath, timeNameType);
                isFirst = false;
            }else{
                FileLineOut.outNewLines(pathTimeLine, lines, filesDirPath, timeNameType);
            }

            StockYmd stockYmd = lastYmdMap.get(stock.getStockId());
            if(stockYmd == null){
                stockYmd = new StockYmd();
                stockYmd.setStock(stock);
                stockYmd.setYmd(Integer.parseInt(endYmd));
                lastYmdMap.put(stock.getStockId(), stockYmd);
            }else{
                stockYmd.setYmd(Integer.parseInt(endYmd));
            }

            if(callCount > 20){
                outLastYmdMap();
                callCount = 0;
            }


            if(endYmdNum >= maxYmd){
                break;
            }
            nextYmd = YmdUtil.getYmd(endYmd, 1);
//            if(lines.length == 0){
//                nextYmd = YmdUtil.getYmd(endYmd, 1);
//            }else{
//
//                nextYmd = YmdUtil.getYmd(TimeLines.getMaxYmd(param.getPathTimeLine(), lines, zoneId),1);
//            }
        }
    }

    public void setListedNullBeginYmd(String listedNullBeginYmd) {
        this.listedNullBeginYmd = listedNullBeginYmd;
    }
}
