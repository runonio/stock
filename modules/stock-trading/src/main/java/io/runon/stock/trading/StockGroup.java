package io.runon.stock.trading;

import lombok.Data;
import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.DateTime;

/**
 * @author macle
 */
@Data
@Table(name="stock_group")
public class StockGroup {

    @PrimaryKey(seq = 1)
    @Column(name = "stock_group_id")
    String stockGroupId;

    @Column(name = "group_type")
    String groupType;

    @Column(name = "country")
    String country;

    @Column(name = "name_ko")
    String nameKo;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "description")
    String description;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();
}
