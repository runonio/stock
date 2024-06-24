import io.runon.stock.securities.firm.api.kor.ls.SpotDailyInvestorOut;
/**
 * 파일처리 관련 유틸성 클래스
 * @author macle
 */
public class LsDailyOut {
    public static void main(String[] args) {

        SpotDailyInvestorOut investorOut =new SpotDailyInvestorOut();
        investorOut.outKor();
        investorOut.outKorDelisted();
    }
}
