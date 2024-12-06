package io.runon.stock.trading.data.management;

import io.runon.commons.utils.FileUtil;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockYmd;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CountryCode;
import io.runon.trading.data.TextLong;
import io.runon.trading.data.json.JsonTimeFile;

import java.time.ZoneId;
import java.util.Collection;
import java.util.Map;
/**
 * @author macle
 */
public class SpotOuts {



    public static void setLastYmdMap(Map<String, StockYmd> lastYmdMap, Stock[] stocks, CountryCode countryCode, StockPathLastTime stockPathLastTime, String interval, ZoneId zoneId){

        Map<String, Stock> stockMap = Stocks.makeMap(stocks);

        String filePath = stockPathLastTime.getLastTimeFilePath(countryCode,interval);
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

    public static void outLastYmdMap(Map<String, StockYmd> lastYmdMap, CountryCode countryCode, StockPathLastTime stockPathLastTime, String interval){
        Collection<StockYmd> stockYmdCollection =  lastYmdMap.values();

        int index = 0;
        TextLong[] idTimes = new TextLong[stockYmdCollection.size()];
        for(StockYmd stockYmd : stockYmdCollection){

            TextLong idTime = new TextLong();
            idTime.setText(stockYmd.getStock().getStockId());
            idTime.setNumber(Stocks.getDailyOpenTime(stockYmd.getStock(), stockYmd.getYmd()));
            idTimes[index++] = idTime;
        }

        String filePath = stockPathLastTime.getLastTimeFilePath(countryCode,interval);
        JsonTimeFile.updateLastTimeLines(idTimes, filePath, TextLong.SORT_DESC);
    }
}
