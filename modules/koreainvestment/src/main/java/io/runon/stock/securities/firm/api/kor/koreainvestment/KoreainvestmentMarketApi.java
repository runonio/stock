package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.commons.callback.StrCallback;
import io.runon.commons.apis.http.HttpApiResponse;
import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.exception.StockApiException;
import io.runon.trading.TradingTimes;
import io.runon.trading.closed.days.ClosedDaysCallback;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 한국투자증권 시장 관련 API 정의
 * API가 많아서 정리한 클래스를 나눈다.
 * @author macle
 */
public class KoreainvestmentMarketApi implements ClosedDaysCallback {
    private final KoreainvestmentApi koreainvestmentApi;
    public KoreainvestmentMarketApi(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;
    }


    public String getClosedDaysJson(String baseYmd){
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/chk-holiday";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","CTCA0903R");

        String query = "?BASS_DT="+ baseYmd +"&CTX_AREA_NK=&CTX_AREA_FK=" ;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();
    }

    public void callbackClosedDays(String beginYmd, String endYmd, StrCallback callback){

        String baseYmd = beginYmd;

        outer:
        for(;;){
            String json = getClosedDaysJson(baseYmd);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("output");
            for (int i = 0; i < array.length(); i++) {

                JSONObject row = array.getJSONObject(i);
                if(row.isNull("bass_dt")){
                    break outer;
                }

                String ymd = row.getString("bass_dt");
                if(YmdUtil.compare(ymd, beginYmd) < 0){
                    baseYmd = YmdUtil.getYmd(ymd,1);
                    continue ;
                }

                int compare = YmdUtil.compare(ymd, endYmd);

                if(compare >  0 ){
                    break outer;
                }

                boolean isClosed = row.getString("opnd_yn").equalsIgnoreCase("N");
                if(isClosed){
                    callback.callback(ymd);
                }

                baseYmd = YmdUtil.getYmd(ymd,1);
                if(compare ==  0 ){
                    break outer;
                }

                if(YmdUtil.compare(baseYmd ,endYmd) > 0){
                    break outer;
                }
                //너무 잦은 호출을 방지하기위한 조건
                koreainvestmentApi.sleep();
            }
        }
    }

    public String [] getClosedDays(String beginYmd, String endYmd){

        List<String> list = new ArrayList<>();

        StrCallback strCallback = list::add;
        callbackClosedDays(beginYmd, endYmd, strCallback);
        String [] days = list.toArray(new String[0]);
        list.clear();

        return days;
    }


    public String getIndexDataJsonText(String indexCode, String period , String ymd){

        String fid_input_iscd;
        if(indexCode.equals("KOSPI")){
            fid_input_iscd = "0001";
        }else if(indexCode.equals("KOSDAQ")){
            fid_input_iscd = "1001";
        }else if(indexCode.equals("KPI200")){
            fid_input_iscd = "2001";
        }else{
            throw new StockApiException("index code error (KOSPI, KOSDAQ, KPI200)  " + indexCode);
        }
        
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/inquire-index-daily-price";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHPUP02120000");

        String query = "?fid_period_div_code=" + period +"&fid_cond_mrkt_div_code=U&fid_input_iscd=" + fid_input_iscd +"&fid_input_date_1=" +ymd ;

        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("code:" + response.getResponseCode() +", " + response.getMessage() +", indexCode: " + indexCode +", ymd: " + ymd);
        }

        return response.getMessage();
    }

    public TradeCandle [] getIndexCandles(String indexCode, String beginYmd, String endYmd){
        //100일씩줌
        String nextBeginYmd = beginYmd;

        int endYmdNum = Integer.parseInt(endYmd);
        String dateFormat = "yyyyMMdd HH:mm";

        List<TradeCandle> list = new ArrayList<>();
        outer:
        for(;;){
            String callYmd = YmdUtil.getYmd(nextBeginYmd,100);
            if(YmdUtil.compare(callYmd,endYmd) > 0){
                callYmd = endYmd;
            }
            int beginYmdNum = Integer.parseInt(nextBeginYmd);

            String jsonText = getIndexDataJsonText(indexCode, "D", callYmd);

            JSONObject object = new JSONObject(jsonText);

            JSONArray array = object.getJSONArray("output2");


            for (int i = array.length() -1; i > -1 ; i--) {
                JSONObject row = array.getJSONObject(i);

                String ymd = row.getString("stck_bsop_date");

                int ymdInt = Integer.parseInt(ymd);

                if(ymdInt < beginYmdNum){
                    continue;
                }

                if(ymdInt > endYmdNum){
                    break outer;
                }


                TradeCandle tradeCandle = new TradeCandle();
                tradeCandle.setOpenTime(Times.getTime(dateFormat, ymd +" 09:00", TradingTimes.KOR_ZONE_ID));
                tradeCandle.setCloseTime(Times.getTime(dateFormat, ymd +" 15:30", TradingTimes.KOR_ZONE_ID));

                tradeCandle.setClose(row.getBigDecimal("bstp_nmix_prpr"));
                tradeCandle.setChange(row.getBigDecimal("bstp_nmix_prdy_vrss"));
                tradeCandle.setPrevious();

                tradeCandle.setOpen(row.getBigDecimal("bstp_nmix_oprc"));
                tradeCandle.setHigh(row.getBigDecimal("bstp_nmix_hgpr"));
                tradeCandle.setLow(row.getBigDecimal("bstp_nmix_lwpr"));

                tradeCandle.setVolume(row.getBigDecimal("acml_vol"));
                tradeCandle.setAmount(row.getBigDecimal("acml_tr_pbmn"));

                if(tradeCandle.getVolume().compareTo(BigDecimal.ZERO) == 0){
                    String nowYmd = YmdUtil.now(TradingTimes.KOR_ZONE_ID);
                    if(ymd.equals(nowYmd)){
                        break outer;
                    }
                }

                list.add(tradeCandle);
            }


            if(YmdUtil.compare(callYmd, endYmd) >= 0){
                break;
            }

            nextBeginYmd = YmdUtil.getYmd(callYmd,1);

            koreainvestmentApi.periodSleep();
        }

        return list.toArray(new TradeCandle[0]);
    }


}