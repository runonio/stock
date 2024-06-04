package io.runon.stock.trading.data.management;

import com.seomse.commons.config.JsonFileProperties;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockYmd;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.TextLong;
import io.runon.trading.data.file.FileLineOut;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.data.file.TimeLines;
import io.runon.trading.data.file.TimeName;
import io.runon.trading.data.json.JsonTimeFile;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.util.Collection;
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

    //장중에 수집하는경우 마지막 라인의 변화를 체크하는 옵션
    //공매도나 대차잔고등 일별집계가 끝나는 데이터는 falst로 설정
    protected boolean isLastLineCheck = true;

    public SpotDailyOut(StockDailyOutParam param){

        this.param = param;
        stockPathLastTime = param.getStockPathLastTime();
    }


    private ZoneId zoneId = TradingConfig.DEFAULT_TIME_ZONE_ID;

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }
    private final Map<String, StockYmd> lastYmdMap = new HashMap<>();

    public void setLastLineCheck(boolean lastLineCheck) {
        isLastLineCheck = lastLineCheck;
    }

    public void setLastYmdMap(Stock [] stocks){

        Map<String, Stock> stockMap = Stocks.makeMap(stocks);

        String filePath = param.getStockPathLastTime().getLastTimeFilePath("1d");
        if(FileUtil.isFile(filePath)){
            TextLong [] idTimes = JsonTimeFile.getLastTimeLines(filePath);

            for(TextLong idTime : idTimes){

                String id = idTime.getText();


                int ymd = Integer.parseInt(YmdUtil.getYmd(idTime.getNumber(), zoneId));

                StockYmd stockYmd = lastYmdMap.get(id);

                if(stockYmd == null){
                    Stock stock = stockMap.get(id);
                    if(stock == null){
                        continue;
                    }

                    stockYmd = new StockYmd(stock, ymd);
                    lastYmdMap.put(id, stockYmd);
                }else{
                    stockYmd.setYmd(Integer.max(ymd, stockYmd.getYmd()));
                }
            }
        }
    }


    public void outLastYmdMap(){
        Collection<StockYmd> stockYmdCollection =  lastYmdMap.values();

        int index = 0;
        TextLong[] idTimes = new TextLong[stockYmdCollection.size()];
        for(StockYmd stockYmd : stockYmdCollection){

            TextLong idTime = new TextLong();
            idTime.setText(stockYmd.getStock().getStockId());
            idTime.setNumber(Stocks.getDailyOpenTime(stockYmd.getStock(), stockYmd.getYmd()));
            idTimes[index++] = idTime;
        }

        String filePath = param.getStockPathLastTime().getLastTimeFilePath("1d");
        JsonTimeFile.updateLastTimeLines(idTimes, filePath, TextLong.SORT_DESC);
    }


    public void out(){
        //전체 종목 일봉 내리기
        //KONEX 는 제외
        String [] exchanges = param.getExchanges();
        Stock [] stocks = Stocks.getStocks(exchanges);
        out(stocks);
    }

    public void out(Stock [] stocks){
        setLastYmdMap(stocks);

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

        outLastYmdMap();
    }

    //상폐된 주식 캔들 내리기
    public void outDelisted(){

        String [] exchanges = param.getExchanges();

        JsonFileProperties jsonFileProperties = param.getJsonFileProperties();

        String delistedYmd = jsonFileProperties.getString(param.getDeletedPropertiesKey(),"19900101");

        String nowYmd = YmdUtil.now(TradingTimes.KOR_ZONE_ID);

        Stock [] stocks = Stocks.getDelistedStocks(exchanges, delistedYmd, nowYmd);

        out(stocks);

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

        String filesDirPath = stockPathLastTime.getFilesDirPath(stock, "1d");

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

            nextYmd = YmdUtil.getYmd(lastTime, TradingTimes.KOR_ZONE_ID);
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
                param.sleep();
                break;
            }

            String endYmd = YmdUtil.getYmd(nextYmd, param.getNextDay());

            int endYmdNum =  Integer.parseInt(endYmd);
            if(endYmdNum > maxYmd){
                endYmd = Integer.toString(maxYmd);
            }

            String [] lines = param.getLines(stock, nextYmd, endYmd);

            if(isLastLineCheck && isFirst) {

                FileLineOut.outBackPartChange(pathTimeLine, lines, filesDirPath, timeNameType, TradingTimes.KOR_ZONE_ID);
                isFirst = false;
            }else{
                FileLineOut.outNewLines(pathTimeLine, lines, filesDirPath, timeNameType, TradingTimes.KOR_ZONE_ID);
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

            if(endYmdNum >= maxYmd){
                param.sleep();
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
