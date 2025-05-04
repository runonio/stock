package dev;

import io.runon.stock.trading.country.kor.KorStocks;
import io.runon.stock.trading.data.management.KorSpotDailyShortSellingOut;
import io.runon.stock.trading.data.management.KorSpotDailyStockLoanOut;
import io.runon.stock.trading.data.management.db.sync.StockDbSync;
import io.runon.trading.data.api.ApiDataSync;

public class RunonDataSync {
    public static void main(String[] args) {

        StockDbSync.getInstance().sync();
        KorStocks.updateStockType();
        ApiDataSync.candleSyncAll();


        //대차잔고
        KorSpotDailyStockLoanOut stockLoanOut = new KorSpotDailyStockLoanOut();
        stockLoanOut.outKor();
        stockLoanOut.outKorDelisted();
//
//            //공매도
        KorSpotDailyShortSellingOut shortSellingOut = new KorSpotDailyShortSellingOut();
        shortSellingOut.outKor();
        shortSellingOut.outKorDelisted();
    }

}
