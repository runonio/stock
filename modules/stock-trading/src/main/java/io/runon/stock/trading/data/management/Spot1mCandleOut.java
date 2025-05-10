package io.runon.stock.trading.data.management;

import io.runon.commons.utils.ExceptionUtil;
import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockYmd;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.StockData;
import io.runon.stock.trading.data.StockDataManager;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.TradingConfig;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.file.LineOutManager;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.data.file.TimeLineLock;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * 현물 1분봉 내리기
 * 우선은 한국 주식 먼저 작업
 * @author macle
 */
@Slf4j
public class Spot1mCandleOut {

    protected final Spot1mCandleOutParam param;

    protected final StockPathLastTime pathLastTime = StockPathLastTime.CANDLE;
    private final PathTimeLine pathTimeLine = PathTimeLine.CSV;

    protected boolean isDelisted = false;

    protected final String interval = "1m";


    public Spot1mCandleOut(Spot1mCandleOutParam param){
        this.param = param;
    }

    protected ZoneId zoneId = TradingConfig.DEFAULT_TIME_ZONE_ID;

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    private final Map<String, StockYmd> lastYmdMap = new HashMap<>();

    public void setLastYmdMap(Stock[] stocks, String exchange){
        SpotOuts.setLastYmdMap(lastYmdMap, stocks, param.getCountryCode(), exchange,pathLastTime, interval, zoneId);
    }
    public void outLastYmdMap(String exchange){
        SpotOuts.outLastYmdMap(lastYmdMap, param.getCountryCode(), exchange, pathLastTime, interval);
    }

    public void setDelisted(boolean delisted) {
        isDelisted = delisted;
    }

    public void out(String beginYmd, String exchange){
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
        out(beginYmd, stocks, exchange);
    }

    public void out(String beginYmd, Stock [] stocks, String exchange){
        setLastYmdMap(stocks, exchange);
        Stocks.sortUseLastTimeParallel(stocks, exchange, interval, pathLastTime);
        int count = 0;

        for(Stock stock : stocks){
            try {
                //같은 데이터를 호출하면 호출 제한이 걸리는 경우가 있다 전체 캔들을 내릴때는 예외처리를 강제해서 멈추지 않는 로직을 추가
                out(beginYmd, stock, exchange);
                param.sleep();
                count++;
                if(count > 10){
                    outLastYmdMap(exchange);
                    count = 0;
                }
            }catch (Exception e){
                log.error(ExceptionUtil.getStackTrace(e) +"\n" + stock);
                try{
                    Thread.sleep(Times.MINUTE_1);
                }catch (Exception ignore){}
            }
        }
        outLastYmdMap(exchange);
    }

    public void out(String beginYmd, Stock stock, String exchange){

        log.debug("1m candle out start: " +stock);

        String nowYmd = YmdUtil.now(zoneId);

        String filesDirPath = pathLastTime.getFilesDirPath(stock, exchange, interval);
        File filesDirFile = new File(filesDirPath);
        //noinspection ResultOfMethodCallIgnored
        filesDirFile.mkdirs();

        long lastTime = pathTimeLine.getLastTime(filesDirPath);

        String nextYmd = null;

        if(lastTime > 0){
            nextYmd = YmdUtil.getYmd(lastTime ,zoneId);
        }

        StockYmd stockYmd = lastYmdMap.get(stock.getStockId());
        if(stockYmd != null){
            if(nextYmd == null){
                nextYmd = Integer.toString(stockYmd.getYmd());
            }else{
                nextYmd = YmdUtil.max(nextYmd, Integer.toString(stockYmd.getYmd()));
            }
        }

        if(nextYmd == null){
            nextYmd = beginYmd;
        }else{
            //일봉은 최대 1년치 1일치를 제공하기 떄문에 너무 과거값은 의미가없음.
            nextYmd = YmdUtil.max(nextYmd, beginYmd);
        }

        for(;;){
            if(YmdUtil.compare(nextYmd, nowYmd) > 0 ){
                break;
            }
            param.sleep();

            TradeCandle [] candles = param.getCandles(stock, nextYmd);
            if(candles == null || candles.length == 0){
                stockYmd = lastYmdMap.get(stock.getStockId());
                if(stockYmd == null){
                    stockYmd = new StockYmd(stock, Integer.parseInt(nextYmd));
                    lastYmdMap.put(stock.getStockId(), stockYmd);
                }else{
                    stockYmd.setYmd(Integer.parseInt(nextYmd));
                }
                nextYmd = YmdUtil.getYmd(nextYmd,1);
                continue;
            }

            //캔들저장
            TimeLineLock timeLineLock = LineOutManager.getInstance().getTimeLineLock(filesDirPath, pathTimeLine);
            timeLineLock.update(CsvCandle.lines(candles));

            stockYmd = lastYmdMap.get(stock.getStockId());
            if(stockYmd == null){
                stockYmd = new StockYmd(stock, Integer.parseInt(nextYmd));
                lastYmdMap.put(stock.getStockId(), stockYmd);
            }else{
                stockYmd.setYmd(Integer.parseInt(nextYmd));
            }
            nextYmd = YmdUtil.getYmd(nextYmd,1);

        }

    }


}
