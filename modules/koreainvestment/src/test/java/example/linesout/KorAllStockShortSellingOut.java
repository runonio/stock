package example.linesout;

import io.runon.stock.trading.data.management.KorSpotDailyShortSellingOut;

/**
 * @author macle
 */
public class KorAllStockShortSellingOut {
    public static void main(String[] args) {
        KorSpotDailyShortSellingOut out = new KorSpotDailyShortSellingOut();
        out.outKor();
        out.outKorDelisted();
    }
}
