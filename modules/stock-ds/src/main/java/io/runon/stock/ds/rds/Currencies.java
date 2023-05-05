package io.runon.stock.ds.rds;

import lombok.Data;
import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.DateTime;
/**
 * @author macle
 */
@Data
@Table(name="currencies")
public class Currencies {

    @PrimaryKey(seq = 1)
    @Column(name = "currency_id")
    String currencyId;

    @Column(name = "name_ko")
    String nameKo;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "candle_path")
    String candlePath;

    @Column(name = "description")
    String description;

    @Column(name = "data_value")
    String dataValue;

    @DateTime
    @Column(name = "updated_at")
    Long updatedAt;
}
