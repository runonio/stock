package io.runon.stock.trading;

import io.runon.jdbc.annotation.*;
import io.runon.trading.data.EqualsNullCheck;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 주식 발행 내역
 * @author macle
 */
@Data
@Table(name="issue_shares_history")
public class IssueSharesHistory {

    @PrimaryKey(seq = 1)
    @Column(name = "row_no") @Sequence(name ="seq_issue_shares_history")
    Long rowNo;

    @Column(name = "stock_id")
    String stockId;

    @Column(name = "stock_type")
    String stockType;

    @Column(name = "par_value")
    BigDecimal parValue;

    @Column(name = "description")
    String description;

    @Column(name = "issue_qty")
    BigDecimal issueQty;

    @Column(name = "issue_ymd")
    Integer issueYmd;

    @Column(name = "listing_ymd")
    Integer listingYmd;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();

    public void setIssueYmd(String issueYmdValue) {
        if(issueYmdValue == null){
            this.issueYmd = null;
            return;
        }
        this.issueYmd = Integer.parseInt(issueYmdValue);
    }

    public void setListingYmd(String listingYmdValue) {
        if(listingYmdValue == null){
            this.listingYmd = null;
            return;
        }
        this.listingYmd = Integer.parseInt(listingYmdValue);
    }

    public boolean equals(IssueSharesHistory target){
        try {
            if (!EqualsNullCheck.equals(stockType, target.stockType)) {
                return false;
            }

            if(!EqualsNullCheck.equals(parValue, target.parValue)){
                return false;
            }

            if(!EqualsNullCheck.equals(issueQty, target.issueQty)){
                return false;
            }

            if (!EqualsNullCheck.equals(issueYmd, target.issueYmd)) {
                return false;
            }

            if (!EqualsNullCheck.equals(listingYmd, target.listingYmd)) {
                return false;
            }
        }catch(Exception ignore){
            return false;
        }
        return true;
    }

}