package io.runon.stock.trading;

import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.exception.SQLRuntimeException;
import com.seomse.jdbc.objects.JdbcObjects;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author macle
 */
public class StockApiDataUtils {

    public static void insert(Connection conn, String stockId, String apiUrl, String apiParam, String dataValue){
        StockApiData  stockApiData= new StockApiData();
        stockApiData.setStockId(stockId);
        stockApiData.setApiUrl(apiUrl);
        stockApiData.setApiParam(apiParam);
        stockApiData.setDataValue(dataValue);
        JdbcObjects.insert(conn, stockApiData);
    }

    public static String getDataValue(Connection conn, String stockId, String apiUrl, String apiParam){
        try {
            return JdbcQuery.getResultOne(conn, "select data_value from stock_api_data where stock_id='" + stockId +"' and api_url='" + apiUrl +"' and api_param='" + apiParam +"'");
        }catch (SQLException e){
            throw new SQLRuntimeException(e);
        }
    }

}
