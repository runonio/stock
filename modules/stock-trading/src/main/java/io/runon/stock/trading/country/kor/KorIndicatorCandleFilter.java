package io.runon.stock.trading.country.kor;

import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.TradeCandleFilter;

import java.math.BigDecimal;

/**
 * 한국주식시장
 * @author macle
 */
public class KorIndicatorCandleFilter implements TradeCandleFilter {



    @Override
    public boolean isFiltering(TradeCandle candle) {





        if(candle.getAmount().compareTo(BigDecimal.ZERO)  == 0){
            return true;
        }

        if(candle.getVolume().compareTo(BigDecimal.ZERO)  == 0){
            return true;
        }


        BigDecimal changePercentAbs = candle.getChangePercent().abs();


        BigDecimal limitPercent =  KorStockMarket.getUpDownLimitPercent(candle.getTime());

        if(changePercentAbs.compareTo(limitPercent) > 0){
            return true;
        }


        return false;
    }



}
