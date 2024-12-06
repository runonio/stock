package dev;

import io.runon.commons.callback.GenericCallBack;
import io.runon.commons.config.Config;
import io.runon.jdbc.objects.JdbcObjects;
import io.runon.stock.trading.StockDailyData;
import io.runon.stock.trading.TreasuryStockTrading;
import io.runon.stock.trading.daily.RdsDaily;
import org.json.JSONObject;

/**
 * @author macle
 */
public class TreasuryStockTradingUpdate {
    public static void main(String[] args) {
        Config.getConfig("");


        GenericCallBack<StockDailyData> callBack = o -> {

            String dataValue = o.getDataValue();
            if(dataValue == null){
                return;
            }
            try{

                JSONObject row = new JSONObject(dataValue);

                TreasuryStockTrading treasuryStockTrading = TreasuryStockTrading.make(dataValue);
                if(!row.isNull("reg_quantity")) {
                    treasuryStockTrading.setRegVolume(row.getBigDecimal("reg_quantity"));
                }
                if(!row.isNull("reg_trading_price")) {
                    treasuryStockTrading.setRegAmount(row.getBigDecimal("reg_trading_price"));
                }
                if(!row.isNull("trading_quantity")) {
                    treasuryStockTrading.setVolume(row.getBigDecimal("trading_quantity"));
                }
                if(!row.isNull("trading_price")) {
                    treasuryStockTrading.setAmount(row.getBigDecimal("trading_price"));
                }

                o.setDataValue(treasuryStockTrading.toString());
                o.setUpdatedAt(System.currentTimeMillis());
                RdsDaily.update(o);

            }catch (Exception e){
                e.printStackTrace();
            }


        };
        JdbcObjects.callbackObj(StockDailyData.class, "data_key='treasury_stock_trading'", callBack);

    }
}
