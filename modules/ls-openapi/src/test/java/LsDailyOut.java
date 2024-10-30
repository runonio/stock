import io.runon.stock.securities.firm.api.kor.ls.LsFuturesDailyCandleOut;
import io.runon.stock.securities.firm.api.kor.ls.LsSpotDailyInvestorOut;
/**
 * 파일처리 관련 유틸성 클래스
 * @author macle
 */
public class LsDailyOut {
    public static void main(String[] args) {

        LsSpotDailyInvestorOut investorOut =new LsSpotDailyInvestorOut();
        investorOut.outKor();
        investorOut.outKorDelisted();

        LsFuturesDailyCandleOut futuresDailyCandleOut = new LsFuturesDailyCandleOut();
        futuresDailyCandleOut.out();

    }
}
