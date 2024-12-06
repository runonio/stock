package io.runon.stock.trading;


import io.runon.jdbc.annotation.Column;
import io.runon.jdbc.annotation.DateTime;
import io.runon.jdbc.annotation.PrimaryKey;
import io.runon.jdbc.annotation.Table;
import io.runon.trading.TradingGson;
import lombok.Data;

/**
 * @author macle
 */
@Data
@Table(name="stock")
public class Stock {


    @PrimaryKey(seq = 1)
    @Column(name = "stock_id")
    String stockId;

    @Column(name = "exchange")
    String exchange;

    @Column(name = "symbol")
    String symbol;

    @Column(name = "stock_type")
    String stockType;

    @Column(name = "isin")
    String isin;

    @Column(name = "cik")
    String cik;

    @Column(name = "name_ko")
    String nameKo;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "description")
    String description;

    @Column(name = "issued_shares")
    Long issuedShares;

    @Column(name = "shares_outstanding")
    Long sharesOutstanding;

    @Column(name = "is_listing")
    Boolean isListing;

    @Column(name = "listed_ymd")
    Integer listedYmd;

    @Column(name = "delisted_ymd")
    Integer delistedYmd;

    @Column(name = "delist_reason")
    String delistReason;


    @Column(name = "founding_ymd")
    Integer foundingYmd;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();

    public boolean isListing(){
        return isListing;
    }

    @Override
    public String toString(){
        return  TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
