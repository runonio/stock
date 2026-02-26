package io.runon.stock.trading.data;

import io.runon.jdbc.annotation.Column;
import io.runon.jdbc.annotation.DateTime;
import io.runon.jdbc.annotation.PrimaryKey;
import io.runon.jdbc.annotation.Table;
import lombok.Data;

/**
 * 주식 초기등록용 기본정보
 * @author macle
 */
@Data
@Table(name="stock")
public class StockReg {

    @PrimaryKey(seq = 1)
    @Column(name = "stock_id")
    String stockId;

    @Column(name = "market")
    String market;

    @Column(name = "symbol")
    String symbol;

    @Column(name = "stock_type")
    String stockType;

    @Column(name = "name_ko")
    String nameKo;

    @Column(name = "name_en")
    String nameEn;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();


}
