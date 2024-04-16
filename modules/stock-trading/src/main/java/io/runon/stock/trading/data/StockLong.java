package io.runon.stock.trading.data;

import io.runon.stock.trading.Stock;
import lombok.Data;

import java.util.Comparator;

/**
 * @author macle
 */
@Data
public class StockLong {

    public static Comparator<StockLong> SORT = Comparator.comparingLong(o -> o.num);

    Stock stock;
    long num ;

    public StockLong(){

    }

    public StockLong(Stock stock, long num){
        this.stock = stock;
        this.num = num;
    }

}
