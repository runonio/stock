package io.runon.stock.trading.technical.analysis.clustering;

import io.runon.commons.data.BigDecimalArray;
import io.runon.commons.data.IdArray;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.candle.StockCandles;
import io.runon.stock.trading.technical.analysis.similarity.StockSimSearchCandle;
import io.runon.trading.TimeChangePercent;
import io.runon.trading.data.TextLong;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.similarity.TimeChangeGet;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;
import java.util.Map;

/**
 * @author macle
 */
public class StockClusteringData extends StockSimSearchCandle {

    public final static Comparator<StockClusteringData> SORT_ASC = Comparator.comparingInt(StockClusteringData::getLength);

    public StockClusteringData(Stock stock){
        super(stock);
    }

    IdArray<BigDecimalArray> [] searchArray;

    public int getLength(){
        if(searchArray == null){
            return 0;
        }
        return searchArray.length;
    }

    private BigDecimal avgChange = null;
    public BigDecimal getAvgChange(){

        if(avgChange != null){
            return avgChange;
        }

        avgChange = TimeChangePercent.getAvg(candles);
        return avgChange;
    }

    public IdArray<BigDecimalArray>[] getSearchArray() {
        return searchArray;
    }


    BigDecimal groupScore;

    public void setGroupScore(Map<Long, BigDecimal> avgMap){
        groupScore = BigDecimal.ZERO;

        if(avgMap.isEmpty() || getChangeArray().length == 0)
            return;

        TimeChangePercent[] timeChangePercents = getChangeArray();
        for(TimeChangePercent timeChangePercent : timeChangePercents){
            BigDecimal avg = avgMap.get(timeChangePercent.getTime());
            groupScore = groupScore.add(timeChangePercent.getChangePercent().subtract(avg));
        }
        groupScore = groupScore.divide(new BigDecimal(timeChangePercents.length), MathContext.DECIMAL128);
    }

    public String getCsvString(){
        StringBuilder sb = new StringBuilder();


        return sb.toString();
    }

}