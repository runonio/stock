package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.http.HttpApiResponse;
import com.seomse.commons.utils.time.Times;
import io.runon.stock.securities.firm.api.kor.koreainvestment.exception.KoreainvestmentApiException;
import io.runon.trading.LockType;
import io.runon.trading.PriceChangeType;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 한국투자증권 기간별 데이터 관련 API 정의
 * API가 많아서 정리한 클래스를 나눈다.
 * @author macle
 */
public class KoreainvestmentPeriodDataApi {

    private final KoreainvestmentApi koreainvestmentApi;
    public KoreainvestmentPeriodDataApi(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;
    }

    /**
     *
     *  fid_cond_mrkt_div_code J : 주식, ETF, ETN
     *
     * @param symbol 종목코드
     *
     * @param period 기간유형 	D:일봉, W:주봉, M:월봉, Y:년봉
     * @param beginYmd 시작년월일
     * @param endYmd 끝 년월일
     * @param isRevisePrice 수정주가 여뷰
     * @return 결과값 jsontext
     */
    public String getPeriodDataJsonText(String symbol, String period, String beginYmd, String endYmd, boolean isRevisePrice){
        //https://apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock-quotations#L_a08c3421-e50f-4f24-b1fe-64c12f723c77

        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHKST03010100");

        //수정주가여부
        String sendRevisePrice;
        if(isRevisePrice){
            sendRevisePrice = "0";
        }else{
            sendRevisePrice = "1";
        }

        String query = "?fid_cond_mrkt_div_code=J&fid_input_iscd=" + symbol +"&fid_input_date_1=" + beginYmd +"&fid_input_date_2=" +endYmd +"&fid_period_div_code=" + period + "&fid_org_adj_prc=" + sendRevisePrice;

        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new KoreainvestmentApiException("code:" + response.getResponseCode() +", " + response.getMessage() +", symbol: " + symbol +", beginYmd: " + beginYmd);
        }

        return response.getMessage();
    }

    /**
     * 일별 신용 정보
     * @param symbol 종복코드
     * @param ymd 결제일자
     * @return 결과값 json
     */
    public String getDailyCreditBalanceJson(String symbol, String ymd){

        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/daily-credit-balance";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHPST04760000");

        String query = "?fid_cond_mrkt_div_code=J&fid_cond_scr_div_code=20476&fid_input_iscd=" + symbol +"&fid_input_date_1=" +ymd ;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new KoreainvestmentApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();

    }

    public TradeCandle [] getCandles(String symbol, String period, String beginYmd, String endYmd, boolean isRevisePrice){
        String jsonText = getPeriodDataJsonText(symbol, period, beginYmd, endYmd, isRevisePrice);
        return getCandles(jsonText);
    }


    public static TradeCandle [] getCandles(String jsonText){

        JSONObject object = new JSONObject(jsonText);
        String code = object.getString("rt_cd");
        if(!code.equals("0")){
            if(!object.isNull("msg1")){
                throw new KoreainvestmentApiException("rt_cd: " + code + ", message: " + object.getString("msg1"));
            }else{
                throw new KoreainvestmentApiException("rt_cd: " + code);
            }
        }

        JSONArray array = object.getJSONArray("output2");

        String dateFormat = "yyyyMMdd hh:mm";


        int length = array.length();

        int candleIndex = 0;
        TradeCandle [] candles = new TradeCandle[length];

        for (int i = length -1; i > -1 ; i--) {

            JSONObject row = array.getJSONObject(i);

            if(row.isNull("stck_bsop_date")){
                //상장 이전데이터를 조회할경우
                return TradeCandle.EMPTY_CANDLES;
            }

            String ymd = row.getString("stck_bsop_date");


            TradeCandle tradeCandle = new TradeCandle();
            tradeCandle.setOpenTime(Times.getTime(dateFormat, ymd +" 09:00", TradingTimes.KOR_ZONE_ID));
            tradeCandle.setCloseTime(Times.getTime(dateFormat, ymd +" 15:30", TradingTimes.KOR_ZONE_ID));
            tradeCandle.setOpen(new BigDecimal(row.getString("stck_oprc")));
            tradeCandle.setHigh(new BigDecimal(row.getString("stck_hgpr")));
            tradeCandle.setLow(new BigDecimal(row.getString("stck_lwpr")));
            tradeCandle.setClose(new BigDecimal(row.getString("stck_clpr")));
            tradeCandle.setVolume(new BigDecimal(row.getString("acml_vol")));
            tradeCandle.setTradingPrice(new BigDecimal(row.getString("acml_tr_pbmn")));
            tradeCandle.setChange(new BigDecimal(row.getString("prdy_vrss")));

            //락 유형
            if(!row.isNull("flng_cls_code")) {
                String clsCode = row.getString("flng_cls_code");
                if(!clsCode.equals("00")){
                    tradeCandle.addData("lock_code", clsCode);

//                     * 01 : 권리락
//                     * 02 : 배당락
//                     * 03 : 분배락
//                     * 04 : 권배락
//                     * 05 : 중간(분기)배당락
//                     * 06 : 권리중간배당락
//                     * 07 : 권리분기배당락

                    if(clsCode.equals("01")){
                        tradeCandle.addData("lock_type", LockType.RIGHTS_LOCK.toString());
                    }else if(clsCode.equals("02")){
                        tradeCandle.addData("lock_type", LockType.DIVIDEND_LOCK.toString());
                    }else if(clsCode.equals("03")){
                        tradeCandle.addData("lock_type", LockType.DISTRIBUTION_LOCK.toString());
                    }else if(clsCode.equals("04")){
                        tradeCandle.addData("lock_type", LockType.RIGHTS_DIVIDEND_LOCK.toString());
                    }else if(clsCode.equals("05")){
                        tradeCandle.addData("lock_type", LockType.DIVIDEND_LOCK.toString());
                    }else if(clsCode.equals("06")){
                        tradeCandle.addData("lock_type", LockType.RIGHTS_DIVIDEND_LOCK.toString());
                    }else if(clsCode.equals("07")){
                        tradeCandle.addData("lock_type", LockType.RIGHTS_DIVIDEND_LOCK.toString());
                    }
                }
            }
            if(!row.isNull("prdy_vrss_sign")) {
                String signValue = row.getString("prdy_vrss_sign");
                if(signValue.equals("1")){
                    tradeCandle.setPriceChangeType(PriceChangeType.RISE);
                    tradeCandle.setPriceLimit(true);
                }else if(signValue.equals("2")){
                    tradeCandle.setPriceChangeType(PriceChangeType.RISE);
                }else if(signValue.equals("3")){
                    tradeCandle.setPriceChangeType(PriceChangeType.HOLD);
                }else if(signValue.equals("4")){
                    tradeCandle.setPriceChangeType(PriceChangeType.FALL);
                    tradeCandle.setPriceLimit(true);
                }else if(signValue.equals("5")){
                    tradeCandle.setPriceChangeType(PriceChangeType.FALL);
                }
            }

            tradeCandle.setChange();
            tradeCandle.setEndTrade();
            candles[candleIndex++] = tradeCandle;

        }

        return candles;
    }


}
