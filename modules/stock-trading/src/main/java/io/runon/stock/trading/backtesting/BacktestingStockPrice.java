package io.runon.stock.trading.backtesting;

import io.runon.trading.backtesting.price.MapPrice;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;

/**
 * 주식전용 베겥스팅 추가도구
 * @author macle
 */
public class BacktestingStockPrice extends MapPrice<TradeCandle> {

    private final BacktestingStockQuantityAccount account;


    private BigDecimal slippage = BigDecimal.ZERO;

    public BacktestingStockPrice(BacktestingStockQuantityAccount account){
        this.account = account;
    }

    @Override
    public BigDecimal getBuyPrice(String id) {
        BigDecimal buyFeeRate = account.getBuyFeeRate(id);
        BigDecimal price = getPrice(id);

        price = price.add(price.multiply(slippage));

        return price.add(price.multiply(buyFeeRate));
    }

    @Override
    public BigDecimal getSellPrice(String id) {
        BigDecimal price = getPrice(id);
        price = price.subtract(price.multiply(slippage));

        BigDecimal sellFeeRate = account.getSellFeeRate(id);
        return price.subtract(price.multiply(sellFeeRate));
    }


    public void setSlippage(BigDecimal slippage) {
        this.slippage = slippage;
    }
}

