package io.runon.stock.trading;

import io.runon.stock.trading.data.StockLong;
import io.runon.trading.CountryCode;
import io.runon.trading.data.candle.CandleDataUtils;
import io.runon.trading.data.csv.CsvTimeFile;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Arrays;

/**
 * 주식 캔들 관련 유틸성 클래스
 * @author macle
 */
public class StockCandles {

    public static int getSpotCandleDirsCount(CountryCode countryCode){

        String dirPath = CandleDataUtils.getStockSpotCandlePath(countryCode);

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


    public static void sortUseLastOpenTime(Stock [] stocks, CountryCode countryCode, String interval){

        String dirPath =  CandleDataUtils.getStockSpotCandlePath(countryCode);
        String fileSeparator = FileSystems.getDefault().getSeparator();

        StockLong[] sortStocks = new StockLong[stocks.length];

        for (int i = 0; i <sortStocks.length ; i++) {
            Stock stock = stocks[i];

            StockLong stockLong = new StockLong();
            stockLong.setStock(stock);
            String filesDirPath = dirPath+fileSeparator+stock.getStockId()+fileSeparator+interval;
            stockLong.setNum(CsvTimeFile.getLastOpenTime(filesDirPath));
            sortStocks[i] = stockLong;

        }

        Arrays.sort(sortStocks, StockLong.SORT);


        for (int i = 0; i <sortStocks.length ; i++) {
            stocks[i] = sortStocks[i].getStock();
        }

    }



}
