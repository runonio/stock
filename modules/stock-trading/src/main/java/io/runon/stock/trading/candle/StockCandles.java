package io.runon.stock.trading.candle;

import com.seomse.commons.parallel.ParallelArrayJob;
import com.seomse.commons.parallel.ParallelArrayWork;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.data.StockLong;
import io.runon.stock.trading.path.StockPaths;
import io.runon.stock.trading.path.StockPathLastTimeCandle;

import io.runon.trading.CountryCode;
import io.runon.trading.CountryUtils;
import io.runon.trading.TradingConfig;
import io.runon.trading.data.DataPathTimeRange;
import io.runon.trading.data.csv.CsvTimeFile;
import io.runon.trading.data.file.Files;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Arrays;

/**
 * 주식 캔들 관련 유틸성 클래스
 * @author macle
 */
public class StockCandles {

    public static int getSpotCandleDirsCount(CountryCode countryCode){

        String dirPath = StockPaths.getSpotCandlePath(countryCode);

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


    public static void sortUseLastOpenTime(Stock[] stocks, CountryCode countryCode, String interval){

        String dirPath =  StockPaths.getSpotCandlePath(countryCode);
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

    public static void sortUseLastTimeParallel(Stock [] stocks, String interval){
        Stocks.sortUseLastTimeParallel(stocks,interval, new StockPathLastTimeCandle());
    }




    public static DataPathTimeRange getSpotCandleTimeRange(String stockId, String interval){

        String countryCode = Stocks.getCountryCode(stockId);


        DataPathTimeRange dataPathTimeRange= Files.getTimeRange(new File(StockPaths.getSpotCandleFilesPath(stockId, interval)));

        if(dataPathTimeRange == null){
            return null;
        }

        dataPathTimeRange.setZoneId(CountryUtils.getZoneId(countryCode));
        return dataPathTimeRange;
    }

}
