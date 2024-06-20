package io.runon.stock.stater;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApis;
import io.runon.stock.securities.firm.api.kor.koreainvestment.SpotDailyCreditLoanOut;
import io.runon.stock.securities.firm.api.kor.ls.SpotDailyInvestorOut;
import io.runon.stock.trading.data.management.KorSpotDailyShortSellingOut;
import io.runon.stock.trading.data.management.KorSpotDailyStockLoanOut;

/**
 * @author macle
 */

public class DailyDataAllOut {

    public static void out(){

        //캔들
        KoreainvestmentApis.korStocksDailyCandleOut();

        //신용
        SpotDailyCreditLoanOut creditLoanOut = new SpotDailyCreditLoanOut();
        creditLoanOut.outKor();
        creditLoanOut.outKorDelisted();

        //외국인 기관 매매동향
        SpotDailyInvestorOut investorOut =new SpotDailyInvestorOut();
        investorOut.outKor();
        investorOut.outKorDelisted();

        //프로그램 매매동향

        //체결강도

        //대주
        KorSpotDailyStockLoanOut stockLoanOut = new KorSpotDailyStockLoanOut();
        stockLoanOut.outKor();
        stockLoanOut.outKorDelisted();

        //공매도
        KorSpotDailyShortSellingOut shortSellingOut = new KorSpotDailyShortSellingOut();
        shortSellingOut.outKor();
        shortSellingOut.outKorDelisted();


        //시장지수

        //시장자금


    }


}
