package io.runon.stock.trading;

import lombok.Data;

import java.util.Comparator;

/**
 * @author macle
 */
@Data
public class StockYmd {

    public final static Comparator<StockYmd> SORT_DESC = (o1, o2) -> Integer.compare(o2.ymd, o1.ymd);
    public final static Comparator<StockYmd> SORT_ASC = Comparator.comparingInt(o -> o.ymd);

    Stock stock;
    Integer ymd;

    public StockYmd(){

    }

    public StockYmd(Stock stock, Integer ymd){
        this.stock = stock;
        this.ymd = ymd;
    }

}
