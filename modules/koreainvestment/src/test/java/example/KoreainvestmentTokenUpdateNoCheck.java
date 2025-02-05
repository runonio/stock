package example;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;

/**
 * 예제
 * @author macle
 */
public class KoreainvestmentTokenUpdateNoCheck {
    public static void main(String[] args) {
        KoreainvestmentApi.getInstance().updateTokenNoCheck();
    }
}
