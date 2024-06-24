package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.stock.trading.Stock;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.trading.CreditLoanDaily;
import io.runon.trading.data.TradingLines;
import io.runon.trading.data.file.PathTimeLine;
import lombok.extern.slf4j.Slf4j;

/**
 * 일별 개별 신용정보 내리기
 * @author macle
 */
@Slf4j
public class SpotDailyCreditLoanOut extends KoreainvestmentDailyOut{

    public SpotDailyCreditLoanOut(){
        super(StockPathLastTime.CREDIT_LOAN, PathTimeLine.JSON);
        dailyOut.setServiceName("koreainvestment_credit_loan");
    }


    @Override
    public String[] getLines(Stock stock, String beginYmd, String endYmd) {

        CreditLoanDaily[] array = periodDataApi.getCreditLoanDailies(stock.getSymbol(), beginYmd, endYmd);
        return TradingLines.getLines(array);
    }

    @Override
    public int getNextDay() {
        return 30;
    }

    public String getDeletedPropertiesKey() {
        return "delisted_stocks_credit_loan_1d";
    }

}
