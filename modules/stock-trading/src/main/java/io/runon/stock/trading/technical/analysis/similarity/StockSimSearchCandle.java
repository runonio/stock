package io.runon.stock.trading.technical.analysis.similarity;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.candle.StockCandles;
import io.runon.trading.TimeChangePercent;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.similarity.TimeChangeGet;

public class StockSimSearchCandle implements TimeChangeGet {

    protected TradeCandle[] candles = null;

    protected String interval = "1d";

    protected long beginTime;
    protected long endTime;

    protected final Stock stock;
    public StockSimSearchCandle(Stock stock){
        this.stock = stock;
    }

    @Override
    public String getId() {
        return stock.getStockId();
    }

    @Override
    public TimeChangePercent[] getChangeArray() {
        if(candles == null){
            candles = StockCandles.getCandles(stock, interval, beginTime, endTime);
        }
        return candles;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }



}
