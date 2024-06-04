package example.linesout;

import io.runon.stock.trading.data.management.KorSpotDailyStockLoanOut;

/**
 * @author macle
 */
public class KorAllStockLoanOut {
    public static void main(String[] args) {
        KorSpotDailyStockLoanOut out = new KorSpotDailyStockLoanOut();
        out.outKor();
        out.outKorDelisted();
    }
}
