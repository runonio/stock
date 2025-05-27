package example.period;

import io.runon.stock.securities.firm.api.kor.koreainvestment.SpotDailyCandleOut;
/**
 * @author macle
 */
public class KorDelistedCandleOut {
    public static void main(String[] args) {
        SpotDailyCandleOut candleOut = new SpotDailyCandleOut();
        candleOut.outKorDelisted();
    }
}
