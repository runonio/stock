package io.runon.stock.trading;

import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;
import lombok.Data;

/**
 * @author macle
 */
@Data
@Table(name="futures")
public class Futures {

    @PrimaryKey(seq = 1)
    @Column(name = "futures_id")
    String futuresId;

    @Column(name = "market_type")
    String marketType;

    @Column(name = "exchange")
    String exchange;

    @Column(name = "name_ko")
    String nameKo;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "currency")
    String currency;

    @Column(name = "candle_path")
    String candlePath;

    @Column(name = "tick_size")
    String tickSize;

    @Column(name = "tick_value")
    String tickValue;

    @Column(name = "symbol")
    String symbol;

    @Column(name = "point_value")
    String pointValue;

    @Column(name = "maturity_month")
    String maturityMonth;

    @Column(name = "contract_size")
    String contractSize;

    @Column(name = "settlement_type")
    String settlementType;

    @Column(name = "settlement_day")
    String settlementDay;

    @Column(name = "last_rollover_day")
    String lastRolloverDay;

    @Column(name = "description")
    String description;

    @Column(name = "data_value")
    String dataValue;

    @DateTime
    @Column(name = "updated_at")
    Long updatedAt;


}
