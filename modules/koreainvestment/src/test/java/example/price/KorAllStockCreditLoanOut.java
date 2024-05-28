package example.price;

import io.runon.stock.securities.firm.api.kor.koreainvestment.SpotDailyCreditLoanOut;

/**
 * @author macle
 */
public class KorAllStockCreditLoanOut {
    public static void main(String[] args) {
        SpotDailyCreditLoanOut out = new SpotDailyCreditLoanOut();
        out.outKor();
        out.outKorDelisted();
    }
}
