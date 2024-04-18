package io.runon.stock.securities.firm.api.kor.koreainvestment;
/**
 * 한국투자증권 시장 관련 API 정의
 * API가 많아서 정리한 클래스를 나눈다.
 * @author macle
 */
public class ClosedDaysFileOutServiceStarter {
    public static void main(String[] args) {
        KoreainvestmentApi.getInstance().closedDaysOut();
    }
}
