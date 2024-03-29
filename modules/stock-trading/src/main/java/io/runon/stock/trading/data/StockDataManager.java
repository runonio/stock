package io.runon.stock.trading.data;

import com.seomse.commons.config.Config;

/**
 * @author macle
 */
public class StockDataManager {

    private static class Singleton {
        private static final StockDataManager instance = new StockDataManager();
    }

    public static StockDataManager getInstance(){
        return Singleton.instance;
    }

    public final DataConnectType dataConnectType = DataConnectType.valueOf(Config.getConfig("io.runon.stock.data.connect.type","db").toUpperCase());

    private final StockData stockData;

    private StockDataManager(){
        if(dataConnectType == DataConnectType.DB){
            stockData = new StockDataJdbc();
        }else{
            stockData = new StockDataApi();
        }
    }

    public DataConnectType getDataConnectType() {
        return dataConnectType;
    }

    public StockData getStockData() {
        return stockData;
    }
}
