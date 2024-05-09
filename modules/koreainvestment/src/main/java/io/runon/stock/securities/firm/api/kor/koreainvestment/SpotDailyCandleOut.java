package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.config.JsonFileProperties;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.candle.StockCandles;
import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.csv.CsvCandleOut;
import io.runon.trading.data.csv.CsvTimeFile;
import io.runon.trading.data.file.TimeName;
import io.runon.trading.technical.analysis.candle.Candles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.extern.slf4j.Slf4j;


/**
 * 현물 일봉 캔들 내리기
 * 한국 투자증권은 아직 분봉과거치를 지원하지 않음
 * 올해 지원예정 중이라고 하였음
 * @author macle
 */
@Slf4j
public class SpotDailyCandleOut {

    protected final KoreainvestmentApi koreainvestmentApi;


    public SpotDailyCandleOut(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;

    }

    public SpotDailyCandleOut(){
        this.koreainvestmentApi = KoreainvestmentApi.getInstance();
    }

    public void outKor(){
        //전체 종목 일봉 내리기
        //KONEX 는 제외
        String [] exchanges = {
                "KOSPI"
                , "KOSDAQ"
        };

        Stock [] stocks = Stocks.getStocks(exchanges);
        StockCandles.sortUseLastTimeParallel(stocks,  "1d");
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
    
    //상폐된 주식 캔들 내리기
    public void outKorDelisted(){
        String [] exchanges = {
                "KOSPI"
                , "KOSDAQ"
        };

        JsonFileProperties jsonFileProperties = koreainvestmentApi.getJsonFileProperties();
        jsonFileProperties.getString("delisted_stocks_ymd");

        String delistedYmd = jsonFileProperties.getString("delisted_stocks_candle_1d","19900101");

        String nowYmd = YmdUtil.now(TradingTimes.KOR_ZONE_ID);

        Stock [] stocks = Stocks.getDelistedStocks(exchanges, delistedYmd, nowYmd);
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

        jsonFileProperties.set("delisted_stocks_candle_1d", nowYmd);

    }

    /**
     * 상장 시점부터 내릴 수 있는 전체 정보를 내린다.
     * @param stock 종목정보
     */
    public void out(Stock stock){
//        String type;
//        if(stock.getStockType().startsWith("ETF")){
//             type = "ETF";
//        }else if(stock.getStockType().startsWith("ETN")){
//            type = "ETN";
//        }else if(stock.getStockType().startsWith("STOCK")){
//            type = "J";
//        }else{
//            throw new StockDataException("unknown stock type: " + stock.getStockType());
//        }

        String nowYmd = YmdUtil.now(TradingTimes.KOR_ZONE_ID);
        int nowYmdNum = Integer.parseInt(nowYmd);
        KoreainvestmentPeriodDataApi periodDataApi = koreainvestmentApi.getPeriodDataApi();

        //초기 데이터는 상장 년원일
        String nextYmd ;

        String filesDirPath = StockPaths.getSpotCandleFilesPath(stock.getStockId(),"1d");

        long lastOpenTime = CsvTimeFile.getLastTime(filesDirPath);

        if(lastOpenTime > -1){
            nextYmd = YmdUtil.getYmd(lastOpenTime, TradingTimes.KOR_ZONE_ID);

            if(stock.getDelistedYmd() != null){
                int lastYmdInt = Integer.parseInt(nextYmd);
                if(lastYmdInt >= stock.getDelistedYmd()){
                    //상폐종목인경우 이미 캔들이 다 저장되어 있을때
                    return ;
                }
            }

        }else{
            if(stock.getListedYmd() == null){
                log.error("listed ymd null: " + stock);
                return ;
            }
            nextYmd = Integer.toString(stock.getListedYmd());

        }

        TimeName.Type timeNameType = TimeName.getCandleType(Times.DAY_1);

        boolean isFirst = true;


        log.debug("start stock: " + stock);

        int maxYmd = nowYmdNum;

        if(stock.getDelistedYmd() != null){
            maxYmd = stock.getDelistedYmd();
        }
        //최대100건
        for(;;){

            if(YmdUtil.compare(nextYmd, nowYmd) > 0){
                break;
            }

            String endYmd = YmdUtil.getYmd(nextYmd, 100);

            int endYmdNum =  Integer.parseInt(endYmd);
            if(endYmdNum > maxYmd){
                endYmd = Integer.toString(maxYmd);
            }

            String text = periodDataApi.getPeriodDataJsonText(stock.getSymbol(),"D", nextYmd, endYmd, true);
            TradeCandle [] candles = KoreainvestmentPeriodDataApi.getCandles(text);

            String [] lines = CsvCandle.lines(candles);

            if(isFirst) {

                CsvCandleOut.outBackPartChange(lines, filesDirPath, timeNameType, TradingTimes.KOR_ZONE_ID);
                isFirst = false;
            }else{
                CsvCandleOut.outNewLines(lines, filesDirPath, timeNameType, TradingTimes.KOR_ZONE_ID);
            }

            if(endYmdNum >= maxYmd){
                break;
            }

            if(candles.length == 0){
                nextYmd = YmdUtil.getYmd(endYmd, 1);
            }else{
                nextYmd = YmdUtil.getYmd(Candles.getMaxYmd(candles, TradingTimes.KOR_ZONE_ID),1);
            }

            koreainvestmentApi.candleOutSleep();
        }
    }

    public static void main(String[] args) {

//        jsonFileProperties.set("delisted_stocks_ymd","20240501");
        String [] exchanges = {
                "KOSPI"
                , "KOSDAQ"
        };

        Stock [] stocks = Stocks.getDelistedStocks(exchanges, "20240503", "20240503");

        for(Stock stock : stocks){
            System.out.println(stock);
        }
        System.out.println(stocks.length);

//        SpotDailyCandleOut spotDailyCandleOut = new SpotDailyCandleOut();
//
//        KoreainvestmentApi.getInstance();
    }

}
