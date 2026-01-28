package io.runon.stock.trading.account;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockHolding;
import io.runon.trading.account.Account;

import java.math.BigDecimal;

/**
 * 주식 게좌
 * @author macle
 */
public interface StockAccount extends Account {
    StockHolding[] getStockHoldings();

    BigDecimal getPrice(String stockId);
}
