package io.runon.stock.trading;

import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;
import io.runon.trading.data.EqualsNullCheck;
import lombok.Data;

/**
 * 주식 api 데이터 원형정보
 * 파싱하기 전의 데이터 정보
 * Api 의 너무 잦은 호출을 막기위해 저장부분과 파싱해서 사용하는 부분을 분리한다.
 * @author macle
 */
@Data
@Table(name="stock_api_data")
public class StockApiData {

    @PrimaryKey(seq = 1)
    @Column(name = "stock_id")
    String stockId;

    @PrimaryKey(seq = 2)
    @Column(name = "api_url")
    String apiUrl;

    @PrimaryKey(seq = 3)
    @Column(name = "api_param")
    String apiParam;

    @Column(name = "data_value")
    String dataValue;

    @DateTime
    @Column(name = "updated_at")
    Long updatedAt;

    public boolean equals(StockApiData target){
        try {
            if (!EqualsNullCheck.equals(dataValue, target.dataValue)) {
                return false;
            }

        }catch(Exception ignore){
            return false;

        }
        return true;
    }

    public static String getData(String stockId, String apiUrl, String apiParam){

        return JdbcQuery.getResultOne("select data_value from stock_api_data where stock_id='" + stockId +"' and api_url='" + apiUrl + "' and api_param='" + apiParam  + "'");

    }

}
