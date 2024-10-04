package io.runon.stock.trading;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 주식과 숫자 정보
 * @author macle
 */
@Data
public class StockNumber {

    public final static Comparator<StockNumber> SORT_DESC = (o1, o2) -> o2.number.compareTo(o1.number);

    Stock stock;
    BigDecimal number;

    public StockNumber(){

    }

    public StockNumber(Stock stock, BigDecimal number){
        this.stock = stock;
        this.number = number;
    }


}
