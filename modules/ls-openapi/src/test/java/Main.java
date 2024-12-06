import io.runon.commons.utils.time.Times;
import io.runon.trading.TradingTimes;
/**
 * 파일처리 관련 유틸성 클래스
 * @author macle
 */
public class Main {
    public static void main(String[] args) {

        String v = Times.ymdhm(1719145678362L, TradingTimes.KOR_ZONE_ID);
        System.out.println(v);
    }
}
