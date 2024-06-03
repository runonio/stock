package io.runon.stock.trading.data;

import com.seomse.commons.config.Config;
import com.seomse.commons.config.JsonFileProperties;
import com.seomse.commons.config.JsonFilePropertiesManager;
import com.seomse.commons.exception.UndefinedException;

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


    private final JsonFileProperties jsonFileProperties;

    public final DataConnectType dataConnectType = DataConnectType.valueOf(Config.getConfig("io.runon.stock.data.connect.type","db").toUpperCase());

    private final StockData stockData;

    private final LoanData loanData;



    private StockDataManager(){

        if(dataConnectType == DataConnectType.DB){
            stockData = new StockDataJdbc();
            loanData = new LoanDataJdbc();
        }else{
            //api 클래스들을 구현후에 다시 정의 형식에 의한 예지만 메모성 작성
//            stockData = new StockDataApi();
            throw new UndefinedException();
        }


        String jsonPropertiesName = "runon_stock.json";
        jsonFileProperties = JsonFilePropertiesManager.getInstance().getByName(jsonPropertiesName);

    }

    public DataConnectType getDataConnectType() {
        return dataConnectType;
    }

    public StockData getStockData() {
        return stockData;
    }

    public LoanData getLoanData() {
        return loanData;
    }

    public JsonFileProperties getJsonFileProperties() {
        return jsonFileProperties;
    }
}
