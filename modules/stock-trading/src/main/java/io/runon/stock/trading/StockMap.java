package io.runon.stock.trading;

import java.util.HashMap;
import java.util.Map;

/**
 * @author macle
 */
public class StockMap {
    private final Map<String, Stock> idMap = new HashMap<>();
    private final Map<String, Stock> symbolMap = new HashMap<>();

    private final Stock [] stocks;

    public StockMap(Stock [] stocks){
        this.stocks = stocks;

        for(Stock stock :stocks){
            idMap.put(stock.getStockId(), stock);
            symbolMap.put(stock.getSymbol(), stock);
        }
    }


    public Stock get(String key){
        Stock stock = idMap.get(key);
        if(stock == null){
            return symbolMap.get(key);
        }else{
            return stock;
        }
    }

    public Stock getId(String id){
        return idMap.get(id);
    }

    public Stock getSymbol(String symbol){
        return symbolMap.get(symbol);
    }

    public Stock[] getStocks() {
        return stocks;
    }

    public void clear(){
        idMap.clear();
        symbolMap.clear();
    }

    public int size(){
        return stocks.length;
    }
}
