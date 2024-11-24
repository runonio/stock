package io.runon.stock.trading;

import com.seomse.commons.parallel.ParallelArrayJob;
import com.seomse.commons.parallel.ParallelArrayWork;
import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.data.StockData;
import io.runon.stock.trading.data.StockDataManager;
import io.runon.stock.trading.data.StockLong;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.CountryCode;
import io.runon.trading.CountryUtils;
import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.Exchanges;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.file.TimeFiles;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;

/**
 * @author macle
 */
public class Stocks {

    public static Stock getStock(CountryCode countryCode, String symbol){
        StockDataManager stockDataManager = StockDataManager.getInstance();
        StockData stockData = stockDataManager.getStockData();
        return stockData.getStock(countryCode.toString() +"_" + symbol);
    }

    public static Stock getStock(String id){
        StockDataManager stockDataManager = StockDataManager.getInstance();
        StockData stockData = stockDataManager.getStockData();
        return stockData.getStock(id);
    }

    public static Stock[] getStocks(String [] exchanges, String [] types){
        StockDataManager stockDataManager = StockDataManager.getInstance();
        StockData stockData = stockDataManager.getStockData();
        return stockData.getStocks(exchanges, types);
    }

    public static Stock[] getStocks(String [] exchanges){
        StockDataManager stockDataManager = StockDataManager.getInstance();
        StockData stockData = stockDataManager.getStockData();
        return stockData.getStocks(exchanges);
    }

    public static Stock [] getDelistedStocks(String[] exchanges, String beginYmd, String endYmd){
        StockDataManager stockDataManager = StockDataManager.getInstance();
        StockData stockData = stockDataManager.getStockData();
        return stockData.getDelistedStocks(exchanges, beginYmd, endYmd);
    }

    public static Map<String, Stock> makeMap(Stock [] stocks){
        Map<String, Stock> map = new HashMap<>();
        for(Stock stock : stocks){
            map.put(stock.getStockId(), stock);
        }
        return map;
    }

    public static Map<String, Stock> makeSymbolMap(Stock [] stocks){
        Map<String, Stock> map = new HashMap<>();
        for(Stock stock : stocks){
            map.put(stock.getSymbol(), stock);
        }
        return map;
    }

    public static String getCountryCode(Stock stock){
        return getCountryCode(stock.getStockId());
    }

    public static String getCountryCode(String stockId){
        return CountryUtils.getCountryCode(stockId);
    }

    public static String [] getIds(Stock [] stocks){
        String [] ids = new String[stocks.length];
        for (int i = 0; i <ids.length ; i++) {
            ids[i] = stocks[i].getStockId();
        }
        return ids;
    }

    public static ZoneId getZoneId(Stock stock){
        return Exchanges.getZoneId(stock.getExchange());
    }

    public static void sortUseLastTimeParallel(Stock [] stocks, String interval, StockPathLastTime stockPathLastTime){

        StockLong[] sortStocks = new StockLong[stocks.length];
        for (int i = 0; i <sortStocks.length ; i++) {
            Stock stock = stocks[i];
            sortStocks[i] = new StockLong();
            sortStocks[i].setStock(stock);
        }

        ParallelArrayWork<StockLong> work = stockLong -> {

            long time = stockPathLastTime.getLastTime(stockLong.getStock(), interval);
            stockLong.setNum(time);
        };

        ParallelArrayJob<StockLong> parallelArrayJob = new ParallelArrayJob<>(sortStocks, work);
        parallelArrayJob.setThreadCount(TradingConfig.getTradingThreadCount());

        parallelArrayJob.runSync();

        Arrays.sort(sortStocks, StockLong.SORT);
        for (int i = 0; i <sortStocks.length ; i++) {
            stocks[i] = sortStocks[i].getStock();
        }
    }

    public static long getDailyOpenTime(Stock stock, int ymd){
        return getDailyOpenTime(stock, Integer.toString(ymd));
    }

    public static long getDailyOpenTime(Stock stock, String ymd){
        CountryCode countryCode = CountryCode.valueOf(getCountryCode(stock));
        return TradingTimes.getDailyOpenTime(countryCode, ymd);
    }



    public static Stock [] filterListedStock(Stock [] stocks, String baseYmd){
        return filterListedStock(stocks, Integer.parseInt(baseYmd));
    }

    public static Stock [] filterListedStock(Stock [] stocks, int baseYmdInt){

        List<Stock> listedList = new ArrayList<>();

        for(Stock stock : stocks){

            //당시에 상장하지 않은종목
            if(stock.getListedYmd() != null && stock.getListedYmd() > baseYmdInt){
                continue;
            }

            //상폐된 종목
            if(stock.getDelistedYmd() != null && stock.getDelistedYmd() <= baseYmdInt){
                continue;
            }

            if(!stock.isListing){
                //지금 상장되어 있지 않으면
                //마지막업데이트 일시를 상장폐지일로 인식한다.

                ZoneId zoneId = Stocks.getZoneId(stock);

                int delYmd = Integer.parseInt(YmdUtil.getYmd(stock.getUpdatedAt(), zoneId));
                if(delYmd <= baseYmdInt){
                    continue;
                }
            }


            listedList.add(stock);
        }

        return listedList.toArray(new Stock[0]);
    }


    public static StockNumber [] getMarketCap(Stock [] stocks){
        StockNumber [] stockNumbers = new StockNumber[stocks.length];
        for (int i = 0; i <stockNumbers.length ; i++) {
            BigDecimal marketCap = getMarketCap(stocks[i]);
            if(marketCap == null){
                marketCap = BigDecimal.ZERO;
            }
            stockNumbers[i] = new StockNumber(stocks[i], marketCap);
        }

        Arrays.sort(stockNumbers, StockNumber.SORT_DESC);

        return stockNumbers;
    }

    public static BigDecimal getMarketCap(Stock stock){
        Long issuedShares = stock.getIssuedShares();
        if(issuedShares == null){
            return BigDecimal.ZERO;
        }

        TradeCandle lastCandle = getLastCandle1d(stock);
        if(lastCandle == null){
            return null;
        }
        return new BigDecimal(issuedShares).multiply(lastCandle.getClose());
    }

    public static TradeCandle getLastCandle1d(Stock stock){
        String filePath = StockPaths.getSpotCandleFilesPath(stock.getStockId(), "1d");
        String lastLine = TimeFiles.getLastLine(filePath);
        if(lastLine == null){
            return null;
        }
        return CsvCandle.make(lastLine, Times.DAY_1);
    }



    public static Stock [] getStocks(StockMap stockMap, String [] stockIds){

        List<Stock> list = new ArrayList<>();

        for(String stockId: stockIds){
            Stock stock = stockMap.getId(stockId);
            if(stock != null){
                list.add(stock);
            }
        }

        Stock [] stocks = list.toArray(new Stock[0]);
        list.clear();

        return stocks;
    }



}
