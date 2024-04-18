package example.account;

import com.seomse.commons.config.Config;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.securities.firm.api.kor.koreainvestment.exception.KoreainvestmentApiException;

import java.math.BigDecimal;

/**
 * 계좌의 현금 잔고 확인하기
 * @author macle
 */
public class AccountD2CashExample {
    public static void main(String[] args) {
        
        //00000000-00 형식의 한국투자증권 계좌번호
        String accountNumber = Config.getConfig("stock.securities.firm.api.kor.koreainvestment.account.number");

        if(accountNumber == null){
            throw new KoreainvestmentApiException("account number null");
        }

        BigDecimal cash = KoreainvestmentApi.getInstance().getAccountApi().getD2Cash(accountNumber);
        System.out.println(cash.toPlainString());
    }
}
