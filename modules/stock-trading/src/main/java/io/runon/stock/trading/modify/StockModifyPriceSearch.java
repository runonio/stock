package io.runon.stock.trading.modify;

import io.runon.commons.parallel.ParallelArrayJob;
import io.runon.commons.parallel.ParallelArrayWork;
import io.runon.commons.utils.time.Times;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.exception.StockDataException;
import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingConfig;
import io.runon.trading.data.modify.ModifyPrice;
import io.runon.trading.technical.analysis.candle.CandlePreviousCandle;
import io.runon.trading.technical.analysis.candle.CandlePreviousCandles;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 수정주가 검색
 * 데이터 검증
 * 수정주가 사유가 발생한경우 전체 캔들을 다시 받아야함
 * @author macle
 */
public class StockModifyPriceSearch {


    private final String [] stockIds;

    private final Map<String, CandlePreviousCandles> map = new HashMap<>();

    private final Object lock = new Object();

    //0.2%
    private BigDecimal errorRate = null;


    private BigDecimal errorPrice = new BigDecimal("1");

    private long beginTime = System.currentTimeMillis() - (Times.DAY_1*365);

    public StockModifyPriceSearch(String [] stockIds){
        this.stockIds = stockIds;
    }

    public StockModifyPriceSearch(CountryCode countryCode){
        if(countryCode == CountryCode.KOR) {
            String[] exchanges = {
                    "KOSPI"
                    , "KOSDAQ"
            };

            Stock[] stocks = Stocks.getStocks(exchanges);
            stockIds = Stocks.getIds(stocks);
        }else{
            throw new StockDataException("Not supported ; " + countryCode.toString());
        }
    }

    public void setErrorRate(BigDecimal errorRate) {
        this.errorRate = errorRate;
    }

    public void setErrorPrice(BigDecimal errorPrice) {
        this.errorPrice = errorPrice;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void search(){

        ParallelArrayWork<String> work = stockId -> {

            String filesDirPath = StockPaths.getSpotCandleFilesPath(stockId,"1d");

            List<CandlePreviousCandle> list =  ModifyPrice.searchLock(filesDirPath, Times.DAY_1,errorRate,errorPrice, beginTime);

            if(list.isEmpty()){
                return;
            }

            CandlePreviousCandles candlePreviousCandles = new CandlePreviousCandles();
            candlePreviousCandles.setId(stockId);
            candlePreviousCandles.setArray(list.toArray(new CandlePreviousCandle[0]));
            synchronized (lock){
                map.put(stockId, candlePreviousCandles);
            }
            list.clear();
//            stockLong.setNum(CsvTimeFile.getLastOpenTime(filesDirPath));
        };

        ParallelArrayJob<String> parallelArrayJob = new ParallelArrayJob<>(stockIds, work);
        parallelArrayJob.setThreadCount(TradingConfig.getTradingThreadCount());

        parallelArrayJob.runSync();
    }


    public Map<String, CandlePreviousCandles> getMap() {
        return map;
    }


}
