package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.path.StockPathLastTime;
import io.runon.stock.trading.path.StockPathLastTimeCandle;
import io.runon.stock.trading.path.StockPathLastTimeCreditLoan;
import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.json.JsonTimeFile;
import lombok.extern.slf4j.Slf4j;

/**
 * 일별 개별 신용정보 내리기
 * @author macle
 */
@Slf4j
public class SpotDailyCreditLoanOut {
    protected final KoreainvestmentApi koreainvestmentApi;


    public SpotDailyCreditLoanOut(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;
    }

    public SpotDailyCreditLoanOut(){
        this.koreainvestmentApi = KoreainvestmentApi.getInstance();
    }


    private final StockPathLastTime stockPathLastTime = new StockPathLastTimeCreditLoan();

    public void outKor(){
        String [] exchanges = {
                "KOSPI"
                , "KOSDAQ"
        };

        Stock[] stocks = Stocks.getStocks(exchanges);
        Stocks.sortUseLastTimeParallel(stocks,"1d", stockPathLastTime);

        for(Stock stock : stocks){
            try {
                //같은 데이터를 호출하면 호출 제한이 걸리는 경우가 있다 전체 캔들을 내릴때는 예외처리를 강제해서 멈추지 않는 로직을 추가
                out(stock);
            }catch (Exception e){
                try{
                    Thread.sleep(5000L);
                }catch (Exception ignore){}
                log.error(ExceptionUtil.getStackTrace(e) +"\n" + stock);
            }
        }

    }


    public void out(Stock stock){

        String nowYmd = YmdUtil.now(TradingTimes.KOR_ZONE_ID);
        int nowYmdNum = Integer.parseInt(nowYmd);
        KoreainvestmentPeriodDataApi periodDataApi = koreainvestmentApi.getPeriodDataApi();

        //초기 데이터는 상장 년원일
        String nextYmd ;

        String filesDirPath = StockPaths.getSpotCreditLoanFilesPath(stock.getStockId(),"1d");

        long lastTime = JsonTimeFile.getLastTime(filesDirPath);





    }



}
