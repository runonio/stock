package io.runon.stock.trading.daily;

import io.runon.stock.trading.Stock;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.Data;

/**
 * 주식 일별 분석 정보
 * @author macle
 */
@Data
public class StockDailyStore {


    private Stock stock;

    private int arrayCount = 1000;

    TradeCandle [] candles;
    StockInvestorDaily [] investorDailies;

    public StockDailyStore(Stock stock){
        this.stock = stock;
    }

    private int standardYmd;


    public void setData(String standardYmd){
        setData(Integer.parseInt(standardYmd));
    }


    public void setData(int standardYmd){

    }

}
