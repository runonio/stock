package io.runon.stock.trading.candle;

import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtils;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.StockLong;
import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.CountryCode;
import io.runon.trading.CountryUtils;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.DataPathTimeRange;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.csv.CsvTimeFile;
import io.runon.trading.data.file.Files;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.io.File;
import java.nio.file.FileSystems;
import java.time.ZoneId;
import java.util.Arrays;

/**
 * 주식 캔들 관련 유틸성 클래스
 * @author macle
 */
public class StockCandles {

    public static TradeCandle[] getDailyCandles(Stock stock, String exchange, int beginYmd, int endYmd){
        return getDailyCandles(stock, exchange, Integer.toString(beginYmd), Integer.toString(endYmd));
    }
    public static TradeCandle[] getDailyCandles(Stock stock, String exchange, String beginYmd, String endYmd){
        ZoneId zoneId = Stocks.getZoneId(stock);

        long beginTime = YmdUtils.getTime(beginYmd, zoneId);
        long endTime = YmdUtils.getTime(endYmd, zoneId) + Times.DAY_1 ;
        String path = StockPaths.getSpotCandleFilesPath(stock.getStockId(), exchange,"1d");
        return CsvCandle.load(path, Times.DAY_1,beginTime, endTime);
    }

    public static TradeCandle [] getCandles(Stock stock, String exchange, String interval, long beginTime, long endTime){
        String path = StockPaths.getSpotCandleFilesPath(stock.getStockId(), exchange, interval);
        return CsvCandle.load(path, TradingTimes.getIntervalTime(interval), beginTime, endTime);
    }




    public static int getSpotCandleDirsCount(CountryCode countryCode, String exchange){

        String dirPath = StockPaths.getSpotCandlePath(countryCode,exchange);

        File file = new File(dirPath);

        if(!file.isDirectory()){
            return 0;
        }

        File [] files = file.listFiles();
        if(files == null || files.length == 0){
            return 0 ;
        }

        int cnt = 0;
        for(File dirFile : files){
            if(dirFile.isDirectory()){
                cnt ++ ;
            }
        }

        return cnt;
    }

    public static void sortUseLastOpenTime(Stock[] stocks, CountryCode countryCode, String exchange, String interval){

        String dirPath =  StockPaths.getSpotCandlePath(countryCode, exchange);
        String fileSeparator = FileSystems.getDefault().getSeparator();

        StockLong[] sortStocks = new StockLong[stocks.length];

        for (int i = 0; i <sortStocks.length ; i++) {
            Stock stock = stocks[i];

            StockLong stockLong = new StockLong();
            stockLong.setStock(stock);
            String filesDirPath = dirPath+fileSeparator+stock.getStockId()+fileSeparator+interval;
            stockLong.setNum(CsvTimeFile.getLastTime(filesDirPath));
            sortStocks[i] = stockLong;

        }

        Arrays.sort(sortStocks, StockLong.SORT);

        for (int i = 0; i <sortStocks.length ; i++) {
            stocks[i] = sortStocks[i].getStock();
        }
    }

    public static DataPathTimeRange getSpotCandleTimeRange(String stockId, String exchange , String interval){

        String countryCode = Stocks.getCountryCode(stockId);


        DataPathTimeRange dataPathTimeRange= Files.getTimeRange(new File(StockPaths.getSpotCandleFilesPath(stockId, exchange, interval)));

        if(dataPathTimeRange == null){
            return null;
        }

        dataPathTimeRange.setZoneId(CountryUtils.getZoneId(countryCode));
        return dataPathTimeRange;
    }
}
