package io.runon.stock.trading.data;

import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.QueryUtils;
import com.seomse.jdbc.objects.JdbcObjects;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.exception.StockDataException;
import io.runon.trading.data.Exchanges;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Stock[] getStocks(String[] exchanges) {
        if(exchanges == null){
            throw new StockDataException("exchange null");
        }

        if(exchanges.length == 0){
            throw new StockDataException("exchange size 0");
        }
        String where = "exchange in " + QueryUtils.whereIn(exchanges) +
                " and is_listing = true";

        return JdbcObjects.getObjList(Stock.class, where).toArray(new Stock[0]);
    }

    @Override
    public Stock[] getStocks(String[] exchanges, String baseYmd) {
        if(exchanges == null){
            throw new StockDataException("exchange null");
        }

        if(baseYmd == null){
            return getStocks(exchanges);
        }

        String where = "exchange in " + QueryUtils.whereIn(exchanges) +
                " and listed_ymd <=" + baseYmd;


        List<Stock> stockList = JdbcObjects.getObjList(Stock.class, where);
        Stock [] stocks = stockList.toArray(new Stock[0]);

        stockList.clear();

        return Stocks.filterListedStock(stocks, baseYmd);
    }

    @Override
    public Stock[] getDelistedStocks(String[] exchanges, String beginYmd, String endYmd) {
        if(exchanges == null){
            throw new StockDataException("exchange null");
        }

        String where = "exchange in " + QueryUtils.whereIn(exchanges) +
                " and delisted_ymd >=" + beginYmd +" and delisted_ymd <= " + endYmd;

        return  JdbcObjects.getObjList(Stock.class, where).toArray(new Stock[0]);
    }

    @Override
    public Stock[] getAllStocks(String[] exchanges, String [] types) {

        StringBuilder where = new StringBuilder();

        if(exchanges != null){
            where.append("exchange in ") .append(QueryUtils.whereIn(exchanges));
        }

        if(types != null){
            if(!where.isEmpty()){
                where.append(" and ");
            }
            where.append("stock_type in ") .append(QueryUtils.whereIn(types));
        }

        if(where.isEmpty()){
            throw new StockDataException("exchange, types null");
        }

        return  JdbcObjects.getObjList(Stock.class, where.toString()).toArray(new Stock[0]);
    }

    @Override
    public String[] getGroupStockIds(String groupId) {
        List<String> list = JdbcQuery.getStringList("select stock_id from stock_group_map where stock_group_id='" + groupId + "'");
        String [] ids = list.toArray(new String[0]);
        list.clear();
        return ids;
    }

    @Override
    public Stock[] getGroupStocks(String groupId) {
        List<Stock> list = JdbcObjects.getObjList(Stock.class, "stock_id in (select stock_id from stock_group_map where stock_group_id='"  + groupId + "')");
        Stock [] stocks = list.toArray(new Stock[0]);
        list.clear();
        return stocks;
    }
}
