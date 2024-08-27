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
@Table(name="indices")
public class Indices {

    @PrimaryKey(seq = 1)
    @Column(name = "index_id")
    String indexId;

    @Column(name = "country")
    String country;

    @Column(name = "stock_group_id")
    String stockGroupId;

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
    long updatedAt = System.currentTimeMillis();
}
