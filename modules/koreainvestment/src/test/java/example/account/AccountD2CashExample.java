package example.account;

import io.runon.commons.config.Config;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.trading.exception.StockApiException;

import java.math.BigDecimal;

/**
 * 계좌의 현금 잔고 확인하기
 * @author macle
 */
public class AccountD2CashExample {
    public static void main(String[] args) {
        


        BigDecimal cash = KoreainvestmentApi.getInstance().getAccountApi().getD2Cash();
        System.out.println(cash.toPlainString());
    }
}
