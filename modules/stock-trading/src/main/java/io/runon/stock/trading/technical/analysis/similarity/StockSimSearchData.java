package io.runon.stock.trading.technical.analysis.similarity;

import io.runon.commons.data.BigDecimalArray;
import io.runon.commons.data.IdArray;
import io.runon.stock.trading.Stock;
import lombok.Data;

import java.util.Map;

/**
 * @author macle
 */
@Data
public class StockSimSearchData {
    Stock[] targetStocks;
    Map<String, Stock> map;
    IdArray<BigDecimalArray>[] searchArray;


}
