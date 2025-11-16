package io.runon.stock.trading.backtesting;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockHoldingQuantity;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.exception.StockDataException;
import io.runon.trading.PriceGet;
import io.runon.trading.backtesting.account.BacktestingHoldingAccount;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 주식전용 베겥스팅 추가도구
 * @author macle
 */
@Setter
public class BacktestingStockQuantityAccount extends BacktestingHoldingAccount<StockHoldingQuantity> {


    private BigDecimal buyFeeRate = new BigDecimal("0.00015");
    private BigDecimal sellFeeRate = new BigDecimal("0.00015");

    private BigDecimal buyTaxRate = BigDecimal.ZERO;
    private BigDecimal sellTaxRate = new BigDecimal("0.0025");

    protected BigDecimal slippage = new BigDecimal("0.001");


    private Map<String, Stock> stockMap = null;

    private final BacktestingStockPrice stockPrice = new BacktestingStockPrice(this);

    public BacktestingStockQuantityAccount() {
        setIdPrice(stockPrice);
    }

    public void setStockMap(Map<String, Stock> stockMap) {
        this.stockMap = stockMap;
    }


    public void setPriceGet(PriceGet priceGet){
        stockPrice.setPriceGet(priceGet);
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
        Stock stock = getStock(id);

        if(stock.getStockType().startsWith("STOCK")){
            //주식류는 세금
            return buyFeeRate.add(buyTaxRate);
        }else{
            return buyFeeRate;
        }
        //주식, ETF, 에 따라서 수수료율이 다름

    }


    public BigDecimal getSellFeeRate(String id){
        Stock stock =  getStock(id);

        if(stock.getStockType().startsWith("STOCK")){
            return sellFeeRate.add(sellTaxRate);
        }else{
            return sellFeeRate;
        }
    }

    public Stock getStock(String id){
        if(stockMap == null){
            stockMap = new HashMap<>();
        }

        Stock stock = stockMap.get(id);
        if(stock == null){
            stock = Stocks.getStock(id);
            stockMap.put(id, stock);
        }

        return stock;
    }


    public void setSlippage(BigDecimal slippage) {
        this.slippage = slippage;
    }

    public BigDecimal getSlippage() {
        return slippage;
    }

    @Override
    public BigDecimal getPrice(String symbol) {
        return stockPrice.getPrice(symbol);
    }

    public BacktestingStockPrice getStockPrice() {
        return stockPrice;
    }
}
