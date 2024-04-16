package io.runon.stock.trading.candle;

import com.seomse.commons.config.Config;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.StockLong;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingConfig;
import io.runon.trading.data.csv.CsvTimeFile;
import io.runon.trading.parallel.ParallelArrayJob;
import io.runon.trading.parallel.ParallelArrayWork;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Arrays;

/**
 * 주식 캔들 관련 유틸성 클래스
 * @author macle
 */
public class StockCandles {

    public static int getSpotCandleDirsCount(CountryCode countryCode){

        String dirPath = getStockSpotCandlePath(countryCode);

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

        String dirPath =  getStockSpotCandlePath(countryCode);
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

    public static void sortUseLastOpenTimeParallel(Stock [] stocks, CountryCode countryCode, String interval){

        String dirPath =  getStockSpotCandlePath(countryCode);
        String fileSeparator = FileSystems.getDefault().getSeparator();
        StockLong[] sortStocks = new StockLong[stocks.length];
        for (int i = 0; i <sortStocks.length ; i++) {
            Stock stock = stocks[i];
            sortStocks[i] = new StockLong();
            sortStocks[i].setStock(stock);
        }

        ParallelArrayWork<StockLong> work = stockLong -> {
            String filesDirPath = dirPath+fileSeparator+stockLong.getStock().getStockId()+fileSeparator+interval;
            stockLong.setNum(CsvTimeFile.getLastOpenTime(filesDirPath));
        };

        ParallelArrayJob<StockLong> parallelArrayJob = new ParallelArrayJob<>(sortStocks, work);
        parallelArrayJob.runSync();

        Arrays.sort(sortStocks, StockLong.SORT);
        for (int i = 0; i <sortStocks.length ; i++) {
            stocks[i] = sortStocks[i].getStock();
        }

    }



    @SuppressWarnings("ConstantValue")
    public static String getStockSpotCandlePath(){
        String nullCode = null;
        return getStockSpotCandlePath(nullCode);
    }

    public static String getStockSpotCandlePath(CountryCode countryCode){
        return getStockSpotCandlePath(countryCode.toString());
    }

    /**
     * 주식 캔들은 종목수가 많아서 국가별로 따로 관리할 수 있는 기능을 추가 한다. (용량분산)
     * 한국 종목만 3천개가 넘고, 미국까지 은 2만개가량의 종목 정보가 저장될 수 있다.
     * @param countryCode 국가코드
     * @return 캔들 폴더 경로
     */
    public static String getStockSpotCandlePath(String countryCode){


        String fileSeparator = FileSystems.getDefault().getSeparator();

        String spotCandleDirPath = null;

        if(countryCode != null && !countryCode.isEmpty()) {
            spotCandleDirPath = Config.getConfig("stock.spot.candle.dir.path." + countryCode);
            //대소문자 인식용
            if(spotCandleDirPath == null){
                spotCandleDirPath = Config.getConfig("stock.spot.candle.dir.path." + countryCode.toLowerCase());
            }
            if(spotCandleDirPath == null){
                spotCandleDirPath = Config.getConfig("stock.spot.candle.dir.path." + countryCode.toUpperCase());
            }
        }

        //국가별로 다르게 설정할 수 잇음
        //국내만 3천개의 종목이 넘고 미국은 만개의종목이 넘으므로 기본경로는 국가 코드가 들어가게 한다.
        if (spotCandleDirPath == null) {

            if(countryCode == null){
                spotCandleDirPath = Config.getConfig("trading.data.path", TradingConfig.getTradingDataPath()) + fileSeparator + "stock" + fileSeparator +"spot" + fileSeparator +"candle";
            }else{
                countryCode = countryCode.toUpperCase();
                spotCandleDirPath = Config.getConfig("trading.data.path", TradingConfig.getTradingDataPath()) + fileSeparator + "stock" + fileSeparator  +countryCode + fileSeparator+"spot" + fileSeparator +"candle";
            }
        }

        return spotCandleDirPath;
    }

}
