import io.runon.stock.securities.firm.api.kor.ls.LsApi;
import io.runon.stock.trading.data.daily.StockInvestorDaily;

/**
 * 파일처리 관련 유틸성 클래스
 * @author macle
 */
public class InvestorDailiesExample {
    public static void main(String[] args) {
        LsApi api = LsApi.getInstance();
        api.updateAccessToken();
        StockInvestorDaily[] dailies  = api.getPeriodDataApi().getInvestorDailies("005930","20241001","20241024");
        for(StockInvestorDaily daily : dailies){
            System.out.println(daily);
        }
    }
}
