package io.runon.stock.trading;

import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;
import io.runon.trading.data.EqualsNullCheck;
import lombok.Data;

/**
 * 주식 일별 정보
 * @author macle
 */
@Data
@Table(name="stock_daily")
public class StockDailyData {

    @PrimaryKey(seq = 1)
    @Column(name = "stock_id")
    String stockId;

    @PrimaryKey(seq = 2)
    @Column(name = "ymd")
    Integer ymd;

    @PrimaryKey(seq = 3)
    @Column(name = "data_key")
    String dataKey;

    @Column(name = "data_value")
    String dataValue;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();

    public boolean equals(StockDailyData target){
        try {
            if (!EqualsNullCheck.equals(dataValue, target.dataValue)) {
                return false;
            }

        }catch(Exception ignore){
            return false;

        }
        return true;
    }

}
