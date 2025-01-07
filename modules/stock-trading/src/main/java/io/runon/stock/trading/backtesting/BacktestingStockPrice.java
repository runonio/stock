package io.runon.stock.trading.backtesting;

import io.runon.trading.PriceGet;
import io.runon.trading.backtesting.price.IdPrice;

import java.math.BigDecimal;

/**
 * 주식전용 베겥스팅 추가도구
 * @author macle
 */
public class BacktestingStockPrice implements IdPrice {

    private final BacktestingStockQuantityAccount account;

    private PriceGet priceGet;


    public BacktestingStockPrice(BacktestingStockQuantityAccount account){
        this.account = account;
    }

    @Override
    public BigDecimal getPrice(String id) {
        return priceGet.getPrice(id);
    }


    @Override
    public BigDecimal getBuyPrice(String id) {
        BigDecimal buyFeeRate = account.getBuyFeeRate(id);
        BigDecimal price = getPrice(id);

        price = price.add(price.multiply(account.getSlippage()));

        return price.add(price.multiply(buyFeeRate));
    }
    @Override
    public BigDecimal getSellPrice(String id) {
        BigDecimal price = getPrice(id);

        if(price == null){
            price = BigDecimal.ZERO;
        }

        price = price.subtract(price.multiply(account.getSlippage()));

        BigDecimal sellFeeRate = account.getSellFeeRate(id);
        return price.subtract(price.multiply(sellFeeRate));
    }


    public void setPriceGet(PriceGet priceGet) {
        this.priceGet = priceGet;
    }

}

