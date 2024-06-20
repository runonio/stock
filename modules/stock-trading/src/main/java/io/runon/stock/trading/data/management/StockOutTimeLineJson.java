package io.runon.stock.trading.data.management;

import io.runon.stock.trading.Stock;

/**
 * @author macle
 */
public interface StockOutTimeLineJson {
    String outTimeLineJsonText(Stock stock);


    static String [] getLines(Stock stock, StockOutTimeLineJson [] array){
        String [] lines = new String[array.length];
        for (int i = 0; i <lines.length ; i++) {
            lines[i] = array[i].outTimeLineJsonText(stock);
        }

        return lines;
    }

}
