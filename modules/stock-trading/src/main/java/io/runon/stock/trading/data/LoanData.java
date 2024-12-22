package io.runon.stock.trading.data;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.data.daily.ShortSellingDaily;
import io.runon.stock.trading.data.daily.StockLoanDaily;


/**
 * 부채 관련 데이터 정리
 * API를 통해서 가져와야 하는데 데이터목록
 * 주식 대처잔고, 공매도 신용잔고
 *
 *
 * @author macle
 */
public interface LoanData {

    StockLoanDaily[] getStockLoanDailies(Stock stock, String beginYmd, String endYmd);

    ShortSellingDaily[] getShortSellingDailies(Stock stock, String beginYmd, String endYmd);

}
