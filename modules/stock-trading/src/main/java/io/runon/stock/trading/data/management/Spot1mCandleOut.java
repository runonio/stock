package io.runon.stock.trading.data.management;

import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockYmd;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.TradingConfig;
import io.runon.trading.data.TextLong;
import io.runon.trading.data.file.TimeLine;
import io.runon.trading.data.json.JsonTimeFile;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * 현물 1분봉 내리기
 * 우선은 한국 주식 먼저 작업
 * @author macle
 */
public class Spot1mCandleOut {

    protected final Spot1mCandleOutParam param;

    protected final StockPathLastTime stockPathLastTime = StockPathLastTime.CANDLE;
    private final TimeLine timeLine = TimeLine.CSV;



    public Spot1mCandleOut(Spot1mCandleOutParam param){
        this.param = param;
    }

    protected ZoneId zoneId = TradingConfig.DEFAULT_TIME_ZONE_ID;


    private final Map<String, StockYmd> lastYmdMap = new HashMap<>();

    public void setLastYmdMap(Stock[] stocks){

        Map<String, Stock> stockMap = Stocks.makeMap(stocks);

        String filePath = stockPathLastTime.getLastTimeFilePath(param.getCountryCode(),"1m");
        if(FileUtil.isFile(filePath)){
            TextLong[] idTimes = JsonTimeFile.getLastTimeLines(filePath);

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

}
