package io.runon.stock.trading.data.management;

import java.util.HashMap;
import java.util.Map;

/**
 * 벡테스팅, 핛급에 쓰이는 주식관련 분석데이터를 메모리에 올려놓고 활용하기 위한 클래스
 * @author macle
 */
public class StockDataLoadStore {

    private final Map<String, StockDataLoad> map = new HashMap<>();

    private String [] stockTypes ={
        "STOCK"
    };

    public void setStockTypes(String[] stockTypes) {
        this.stockTypes = stockTypes;
    }

    public StockDataLoadStore(){

    }


}
