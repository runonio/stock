package example.linesout;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApis;
import io.runon.stock.securities.firm.api.kor.koreainvestment.SpotDailyCreditLoanOut;
import io.runon.stock.trading.data.management.KorSpotDailyShortSellingOut;
import io.runon.stock.trading.data.management.KorSpotDailyStockLoanOut;

/**
 * @author macle
 */
public class KorAllStockSpotDailyOut {
    public static void main(String[] args) {
        //캔들
        KoreainvestmentApis.korStocksDailyCandleOut();

        //신용
        SpotDailyCreditLoanOut creditLoanOut = new SpotDailyCreditLoanOut();
        creditLoanOut.outKor();
        creditLoanOut.outKorDelisted();
        //매매동향

        //대주
        KorSpotDailyStockLoanOut stockLoanOut = new KorSpotDailyStockLoanOut();
        stockLoanOut.outKor();
        stockLoanOut.outKorDelisted();

        //공매도
        KorSpotDailyShortSellingOut shortSellingOut = new KorSpotDailyShortSellingOut();
        shortSellingOut.outKor();
        shortSellingOut.outKorDelisted();
    }
}
