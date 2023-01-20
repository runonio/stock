package io.runon.stock.ds.issue;

import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Sequence;
import com.seomse.jdbc.annotation.Table;
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
    @Column(name = "row_no")@Sequence(name ="seq_issue_shares_history")
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
    String issueYmd;

    @Column(name = "listing_ymd")
    String listingYmd;


    public boolean equals(IssueSharesHistory target){
        try {
            if (!stockType.equals(target.stockType)) {
                return false;
            }

            if(parValue.compareTo(target.parValue) != 0){
                return false;
            }

            if(issueQty.compareTo(target.issueQty) != 0){
                return false;
            }

            if (!issueYmd.equals(target.issueYmd)) {
                return false;
            }

            if (!listingYmd.equals(target.listingYmd)) {
                return false;
            }
        }catch(Exception ignore){
            return false;

        }
        return true;
    }
    
}
