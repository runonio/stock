package io.runon.stock.stater;

import com.seomse.commons.config.Config;
import io.runon.stock.securities.firm.api.kor.koreainvestment.*;
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

            //개별종목 프로그램 매매동향
            SpotDailyProgramTradeOut programTradeOut = new SpotDailyProgramTradeOut();
            programTradeOut.outKor();
            programTradeOut.outKorDelisted();

            //개별종목 체결강도
            SpotDailyVolumePowerOut volumePowerOut = new SpotDailyVolumePowerOut();
            volumePowerOut.outKor();
            volumePowerOut.outKorDelisted();

            //시장 캔들 (코스피, 코스닥)
            KorIndexDailyOut korIndexDailyOut = new KorIndexDailyOut();
            korIndexDailyOut.out();

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

        //시장지수 내리기
        //

        // 이후 작업목록
        // 시장별매매동향
        // 선물 옵션 매매동향
        // 선물옵션과 같이 분석해야 하지만 선물옵션 정보를 정확히 파악하기위한 사전 지식이 필요함
        // 선물옵션 코드는 어떻게 관리하는지 6월물 7월물등 월물이 변경되는것, 만기일 처리등에 대한 방안을 적립한후 적용



    }

    public static void main(String[] args) {
        Config.getConfig("");
        out();
    }


}
