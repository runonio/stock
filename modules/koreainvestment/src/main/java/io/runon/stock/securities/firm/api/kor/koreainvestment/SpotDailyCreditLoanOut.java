package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.json.JsonTimeFile;

/**
 * 일별 개별 신용정보 내리기
 * @author macle
 */
public class SpotDailyCreditLoanOut {
    protected final KoreainvestmentApi koreainvestmentApi;


    public SpotDailyCreditLoanOut(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;
    }

    public SpotDailyCreditLoanOut(){
        this.koreainvestmentApi = KoreainvestmentApi.getInstance();
    }


    public void outKor(){
        String [] exchanges = {
                "KOSPI"
                , "KOSDAQ"
        };

        Stock[] stocks = Stocks.getStocks(exchanges);




    }


    public void out(Stock stock){

        String nowYmd = YmdUtil.now(TradingTimes.KOR_ZONE_ID);
        int nowYmdNum = Integer.parseInt(nowYmd);
        KoreainvestmentPeriodDataApi periodDataApi = koreainvestmentApi.getPeriodDataApi();

        //초기 데이터는 상장 년원일
        String nextYmd ;

        String filesDirPath = StockPaths.getSpotCreditLoanFilesPath(stock.getStockId(),"1d");

        long lastOpenTime = JsonTimeFile.getLastTime(filesDirPath);



    }



}
