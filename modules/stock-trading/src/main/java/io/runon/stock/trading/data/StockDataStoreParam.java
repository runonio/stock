package io.runon.stock.trading.data;

import lombok.Data;

/**
 * @author macle
 */
@Data
public class StockDataStoreParam {

    int investorDayGap = -1;

    //공매도
    int shortSellingDayGap = -2;

    //프로그램
    int programDayGap = 0;

    //대차잔고
    int stockLoanDayGap = -1;

    //종목별 신용
    int stockCreditLoanDayGap = -1;

    //체결강도
    int volumePowerDayGap = 0;


}
