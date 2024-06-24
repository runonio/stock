package io.runon.stock.stater;

import com.seomse.commons.config.Config;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApis;
import io.runon.stock.securities.firm.api.kor.koreainvestment.SpotDailyCreditLoanOut;
import io.runon.stock.securities.firm.api.kor.koreainvestment.SpotDailyProgramTradeOut;
import io.runon.stock.securities.firm.api.kor.ls.SpotDailyInvestorOut;
import io.runon.stock.trading.data.management.KorSpotDailyShortSellingOut;
import io.runon.stock.trading.data.management.KorSpotDailyStockLoanOut;

/**
 * @author macle
 */

public class DailyDataAllOut {

    public static void out(){

        new Thread(() -> {

            //한국 투자증권 활용부분
            //캔들
            KoreainvestmentApis.korStocksDailyCandleOut();

            //신용
            SpotDailyCreditLoanOut creditLoanOut = new SpotDailyCreditLoanOut();
            creditLoanOut.outKor();
            creditLoanOut.outKorDelisted();

            SpotDailyProgramTradeOut programTradeOut = new SpotDailyProgramTradeOut();
            programTradeOut.outKor();
            programTradeOut.outKorDelisted();

        }).start();


        new Thread(() -> {
            //이베스트 투자증권에서 실행하는 부분은 다른 thread
            //외국인 기관 매매동향
            SpotDailyInvestorOut investorOut =new SpotDailyInvestorOut();
            investorOut.outKor();
            investorOut.outKorDelisted();

        }).start();


        new Thread(() -> {
            //런온 api 호출부분
            //대주
            KorSpotDailyStockLoanOut stockLoanOut = new KorSpotDailyStockLoanOut();
            stockLoanOut.outKor();
            stockLoanOut.outKorDelisted();

            //공매도
            KorSpotDailyShortSellingOut shortSellingOut = new KorSpotDailyShortSellingOut();
            shortSellingOut.outKor();
            shortSellingOut.outKorDelisted();
        }).start();



        //체결강도

        //시장지수

        //시장자금


    }

    public static void main(String[] args) {
        Config.getConfig("");
        out();
    }


}
