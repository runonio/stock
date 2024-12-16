package io.runon.stock.trading.technical.analysis.clustering;

import io.runon.commons.data.BigDecimalArray;
import io.runon.commons.data.IdArray;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.technical.analysis.similarity.StockSimSearchCandle;
import io.runon.trading.TimeChangePercent;
import io.runon.trading.TradingHalt;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Map;

/**
 * @author macle
 */
public class StockClusteringData extends StockSimSearchCandle {

    public final static Comparator<StockClusteringData> SORT_ASC = Comparator.comparingInt(StockClusteringData::getLength);

    public final static Comparator<StockClusteringData> GROUP_SCORE_DESC =  (o1, o2) -> o2.groupScore.compareTo(o1.groupScore);




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

        avgChange = TimeChangePercent.getAvg(candles, true).stripTrailingZeros();
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

            BigDecimal addScore = timeChangePercent.getChangePercent().subtract(avg);
            if(addScore.compareTo(BigDecimal.ONE) > 0){
                addScore = BigDecimal.ONE;
            }

            groupScore = groupScore.add(addScore);
        }
        groupScore = groupScore.divide(new BigDecimal(timeChangePercents.length), MathContext.DECIMAL128).stripTrailingZeros();
    }

    public String getCsvString(){
        return stock.getSymbol() + "," + stock.getNameKo() + "," + groupScore.setScale(6, RoundingMode.HALF_UP).toPlainString() + "," + avgChange.setScale(4, RoundingMode.HALF_UP).toPlainString() + "," + candles.length;
    }

    public BigDecimal getGroupScore() {
        return groupScore;
    }

    //유효성 체크
    public boolean isValid(StockClustering clustering){
        if(candles.length< 2){
            return false;
        }

        if(TradingHalt.isHalt(candles)){
            return false;
        }

        if(candles.length < clustering.minCount){
            return false;
        }


        int minCount = clustering.minCount;
        int cnt = 0;
        for(TradeCandle candle : candles){
            if(candle.getAmount().compareTo(clustering.minAmount) > 0){
                cnt ++;
            }
        }

        if(cnt < minCount){
            return false;
        }


        //추가 유효성 검증이 들어갈 수 있어서 코드 유지
        return true;
    }



}