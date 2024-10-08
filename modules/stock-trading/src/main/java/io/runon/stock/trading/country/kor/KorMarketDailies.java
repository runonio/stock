package io.runon.stock.trading.country.kor;

import io.runon.stock.trading.daily.StockInvestorDaily;
import io.runon.stock.trading.market.KorMarketCreditLoanDaily;
import io.runon.stock.trading.market.MarketFundDaily;
import io.runon.trading.CountryCode;
import io.runon.trading.data.DailyData;
import io.runon.trading.data.daily.DailyDataJdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author macle
 */
public class KorMarketDailies {

    //시장 자산
    public static final int MARKET_FUND_DAILY_GAP = -1;

    public static final int MARKET_CREDIT_LOAN = -1;

    public static MarketFundDaily [] getMarketFund(int beginYmd, int endYmd){
        List<DailyData> list = DailyDataJdbc.getDataList("market_fund", beginYmd, endYmd);
        MarketFundDaily [] dailies = new MarketFundDaily[list.size()];

        for (int i = 0; i <dailies.length ; i++) {
            DailyData dailyData = list.get(i);
            dailies[i] = MarketFundDaily.make(dailyData.getDataValue(), CountryCode.KOR);
        }

        return dailies;
    }

    public static MarketFundDaily [] getMarketFund(MarketFundDaily [] lastDailies, int beginYmd, int endYmd){
        if(lastDailies == null || lastDailies.length < 2){
            return getMarketFund( beginYmd, endYmd);
        }

        //라스트 1개는 다시 불러 온다. 값이 변경될 수 있다.
        List<MarketFundDaily> list = new ArrayList<>();

        int end = lastDailies.length-1;

        for (int i = 0; i < end; i++) {
            MarketFundDaily daily = lastDailies[i];

            int ymd = daily.getYmd();
            if(ymd < beginYmd){
                continue;
            }
            if(ymd > endYmd){
                continue;
            }
            list.add(daily);
        }


        int newBeginYmd = lastDailies[lastDailies.length - 1 ].getYmd();

        if(newBeginYmd < beginYmd){
            newBeginYmd = beginYmd;
        }

        if(newBeginYmd >= endYmd){
            return list.toArray(new MarketFundDaily[0]);
        }

        MarketFundDaily [] dailies = getMarketFund( newBeginYmd, endYmd);
        Collections.addAll(list, dailies);

        return list.toArray(new MarketFundDaily[0]);
    }

    public static KorMarketCreditLoanDaily [] getMarketCreditLoan(int beginYmd, int endYmd){
//
        List<DailyData> list = DailyDataJdbc.getDataList("kor_market_credit_loan", beginYmd, endYmd);

        KorMarketCreditLoanDaily [] dailies = new KorMarketCreditLoanDaily[list.size()];
        for (int i = 0; i <dailies.length ; i++) {
            DailyData dailyData = list.get(i);
            dailies[i] = KorMarketCreditLoanDaily.make(dailyData.getDataValue(), CountryCode.KOR);
        }

        return dailies;
    }

    public static KorMarketCreditLoanDaily [] getMarketCreditLoan(KorMarketCreditLoanDaily [] lastDailies, int beginYmd, int endYmd){
        if(lastDailies == null || lastDailies.length < 2){
            return getMarketCreditLoan( beginYmd, endYmd);
        }

        //라스트 1개는 다시 불러 온다. 값이 변경될 수 있다.
        List<KorMarketCreditLoanDaily> list = new ArrayList<>();

        int end = lastDailies.length-1;

        for (int i = 0; i < end; i++) {
            KorMarketCreditLoanDaily daily = lastDailies[i];

            int ymd = daily.getYmd();
            if(ymd < beginYmd){
                continue;
            }
            if(ymd > endYmd){
                continue;
            }
            list.add(daily);
        }


        int newBeginYmd = lastDailies[lastDailies.length - 1 ].getYmd();

        if(newBeginYmd < beginYmd){
            newBeginYmd = beginYmd;
        }

        if(newBeginYmd >= endYmd){
            return list.toArray(new KorMarketCreditLoanDaily[0]);
        }

        KorMarketCreditLoanDaily [] dailies = getMarketCreditLoan( newBeginYmd, endYmd);
        Collections.addAll(list, dailies);

        return list.toArray(new KorMarketCreditLoanDaily[0]);
    }


    public static void main(String[] args) {
//        MarketFundDaily [] dailies = getMarketFund(20240901, 20240930);
//        for(MarketFundDaily daily : dailies){
//            System.out.println(daily);
//        }

        KorMarketCreditLoanDaily [] dailies = getMarketCreditLoan(20240901, 20240930);
        for(KorMarketCreditLoanDaily daily :dailies){
            System.out.println(daily);
        }
    }

}
