package io.runon.stock.securities.firm.api.kor.koreainvestment;
/**
 * 기간별 가격 데이터와 정보
 * 한국투자증권 오리지날 데이터
 * @author macle
 */
public class KoreainvestmentApis {

    /**
     * 한국 시장 전체종목
     * 호출 시점의 상장한 회사를 기점으로 한다
     * 상장폐지 회사는 호출하지 않음
     */
    public static void korStocksDailyCandleOut() {
        SpotDailyCandleOut candleOut = new SpotDailyCandleOut();
        candleOut.outKor();
    }

}
