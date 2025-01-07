package io.runon.stock.trading;

import io.runon.trading.HoldingQuantity;

import java.math.BigDecimal;

/**
 * 보유종목과 수량정보
 *
 */
public class StockHoldingQuantity implements HoldingQuantity {

    protected final Stock stock;

    protected BigDecimal quantity = BigDecimal.ZERO;


    public StockHoldingQuantity(Stock stock){
        this.stock = stock;
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



}
