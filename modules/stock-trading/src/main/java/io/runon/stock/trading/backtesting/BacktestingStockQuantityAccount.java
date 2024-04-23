package io.runon.stock.trading.backtesting;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockHoldingQuantity;
import io.runon.stock.trading.exception.StockDataException;
import io.runon.trading.backtesting.account.BacktestingHoldingAccount;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 주식전용 베겥스팅 추가도구
 * @author macle
 */
public class BacktestingStockQuantityAccount extends BacktestingHoldingAccount<StockHoldingQuantity> {


    private BigDecimal buyFeeRate = new BigDecimal("0.00015");
    private BigDecimal sellFeeRate = new BigDecimal("0.00015");

    private BigDecimal buyTaxRate = BigDecimal.ZERO;
    private BigDecimal sellTaxRate = new BigDecimal("0.0025");



    private Map<String, Stock> stockMap = null;

    BacktestingStockPrice symbolPrice = new BacktestingStockPrice(this);

    public BacktestingStockQuantityAccount() {
        setSymbolPrice(symbolPrice);
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

    //구매 수수료
    public BigDecimal getBuyFeeRate(String id){

        Stock stock = stockMap.get(id);


        if(stock.getStockType().startsWith("STOCK")){
            //주식류는 세금
            return buyFeeRate.add(buyTaxRate);
        }else{
            return buyFeeRate;
        }
        //주식, ETF, 에 따라서 수수료율이 다름

    }


    public BigDecimal getSellFeeRate(String id){
        Stock stock = stockMap.get(id);

        if(stock.getStockType().startsWith("STOCK")){
            return sellFeeRate.add(sellTaxRate);
        }else{
            return sellFeeRate;
        }
    }
    


    public BacktestingStockPrice getSymbolPrice() {
        return symbolPrice;
    }
}
