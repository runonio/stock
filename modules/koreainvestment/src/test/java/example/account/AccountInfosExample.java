package example.account;

import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.account.KoreainvestmentAccountApi;

/**
 * 계좌의 현금 잔고 확인하기
 * @author macle
 */
public class AccountInfosExample {
    public static void main(String[] args) {

        KoreainvestmentAccountApi accountApi = KoreainvestmentApi.getInstance().getAccountApi();
        String text = accountApi.getInquireBalanceJsonText();

        

        System.out.println(text);

    }
}
