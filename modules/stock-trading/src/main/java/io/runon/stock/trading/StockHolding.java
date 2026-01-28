package io.runon.stock.trading;

import io.runon.trading.HoldingQuantity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 보유종목과 수량정보
 *
 */
@Data
public class StockHolding implements HoldingQuantity {

    protected Stock stock;

    protected BigDecimal quantity = BigDecimal.ZERO;
    protected BigDecimal avgPrice;


    public StockHolding(Stock stock){
        this.stock = stock;
    }

    public StockHolding(){

    }


    @Override
    public String getId() {
        return stock.getStockId();
    }

    @Override
    public BigDecimal getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    @Override
    public String toString(){
        return stock.getStockId() +"," + stock.getNameKo() +"," + quantity.toPlainString() +"," + avgPrice.toPlainString();
    }


}
