package io.runon.stock.securities.firm.api.kor.koreainvestment.jdbc;

import io.runon.jdbc.annotation.Column;
import io.runon.jdbc.annotation.DateTime;
import io.runon.jdbc.annotation.PrimaryKey;
import io.runon.jdbc.annotation.Table;
import lombok.Data;
/**
 * 한국투자증권을 사용할 때 해외주식 정보 (업데이트용)
 * @author macle
 */
@Data
@Table(name="stock")
public class OverseasStockUpdateInfo {
    @PrimaryKey(seq = 1)
    @Column(name = "stock_id")
    String stockId;


    @Column(name = "isin")
    String isin;


    @Column(name = "is_listing")
    Boolean isListing;


    @Column(name = "issued_shares")
    Long issuedShares;

    @Column(name = "listed_ymd")
    Integer listedYmd;

    @Column(name = "delisted_ymd")
    Integer delistedYmd;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();
}
