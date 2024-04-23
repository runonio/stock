package io.runon.stock.trading.backtesting;

import io.runon.trading.backtesting.price.symbol.MapSymbolPrice;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;

/**
 * 주식전용 베겥스팅 추가도구
 * @author macle
 */
public class BacktestingStockPrice extends MapSymbolPrice<TradeCandle> {

    private final BacktestingStockQuantityAccount account;

    public BacktestingStockPrice(BacktestingStockQuantityAccount account){
        this.account = account;
    }

    @Override
    public BigDecimal getBuyPrice(String symbol) {
        return null;
    }

    @Override
    public BigDecimal getSellPrice(String symbol) {
        return null;
    }
}

