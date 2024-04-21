package io.runon.stock.trading.backtesting;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockHoldingQuantity;
import io.runon.stock.trading.exception.StockDataException;
import io.runon.trading.backtesting.account.BacktestingHoldingAccount;
import io.runon.trading.backtesting.price.symbol.SymbolPrice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 주식전용 베겥스팅 추가도구
 * 트레이딩에서 사용하는건 암호화폐와 주식에서의 공통.
 *
 * @author macle
 */
public class BacktestingStockQuantityAccount extends BacktestingHoldingAccount<StockHoldingQuantity> {


    private Map<String, Stock> stockMap = null;

    public BacktestingStockQuantityAccount(SymbolPrice symbolPrice) {
        super(symbolPrice);
    }

    public void setStockMap(Map<String, Stock> stockMap) {
        this.stockMap = stockMap;
    }

    public void putStock(Stock stock){
        if(stockMap == null){
            stockMap = new HashMap<>();
        }

        stockMap.put(stock.getStockId(), stock);
    }


    @Override
    public StockHoldingQuantity newHoldingQuantity(String id, BigDecimal quantity) {

        Stock stock = stockMap.get(id);

        if(stock == null){
            throw new StockDataException("stock map not set stock id: " + id);
        }

        StockHoldingQuantity holdingQuantity = new StockHoldingQuantity(stock);

        holdingQuantity.setQuantity(quantity);

        return holdingQuantity;
    }
}
