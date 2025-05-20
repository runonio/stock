package io.runon.stock.trading.group;

import io.runon.commons.utils.GsonUtils;
import io.runon.jdbc.annotation.Column;
import io.runon.jdbc.annotation.DateTime;
import io.runon.jdbc.annotation.PrimaryKey;
import io.runon.jdbc.annotation.Table;
import lombok.Data;

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

    @Override
    public String toString(){
        return  GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
