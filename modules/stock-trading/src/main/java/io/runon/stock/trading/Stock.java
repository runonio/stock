package io.runon.stock.trading;


import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;
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

    @Column(name = "founding_ymd")
    Integer foundingYmd;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt;
    @Override
    public String toString(){
        return  TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
