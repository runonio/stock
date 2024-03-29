package io.runon.stock.trading.data;

import com.seomse.jdbc.QueryUtils;
import com.seomse.jdbc.objects.JdbcObjects;
import io.runon.stock.trading.Stock;

/**
 * @author macle
 */
public class StockDataJdbc implements StockData{
    @Override
    public Stock getStock(String id) {
        return JdbcObjects.getObj(Stock.class, "stock_id='" + id +"'");
    }

    @Override
    public Stock[] getStocks(String[] exchanges, String[] types) {

        if(exchanges != null && exchanges.length == 0){
            exchanges = null;
        }

        if(types != null && types.length == 0){
            types = null;
        }

        if(exchanges == null && types == null){
            return JdbcObjects.getObjList(Stock.class, "is_listing = true").toArray(new Stock[0]);
        }

        StringBuilder where = new StringBuilder();

        if(exchanges != null){
            where.append("exchange in ") .append(QueryUtils.whereIn(exchanges));
        }

        if(types != null){
            if(where.length() > 0){
                where.append(" and ");
            }
            where.append("stock_type in ") .append(QueryUtils.whereIn(types));
        }

        where.append(" and is_listing = true");

       return JdbcObjects.getObjList(Stock.class, where.toString()).toArray(new Stock[0]);
    }
}
