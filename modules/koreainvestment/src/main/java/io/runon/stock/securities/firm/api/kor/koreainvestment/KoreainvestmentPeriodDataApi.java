package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.commons.apis.http.HttpApiResponse;
import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.data.daily.ProgramDaily;
import io.runon.stock.trading.data.daily.StockCreditLoanDaily;
import io.runon.stock.trading.exception.StockApiException;
import io.runon.trading.CountryCode;
import io.runon.trading.LockType;
import io.runon.trading.PriceChangeType;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.daily.VolumePowerDaily;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.TradeCandleDataKey;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    public String getPeriodDataJsonText(String symbol, String market, String period, String beginYmd, String endYmd, boolean isRevisePrice){
        //https://apiportal.koreainvestment.com/apiservice-apiservice?/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice

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

        market = market.toUpperCase();

        if(market.equals("KRX")){
            market = "J";
        }else if(market.equals("NXT")){
            market = "NX";
        }

        String query = "?fid_cond_mrkt_div_code=" + market +"&fid_input_iscd=" + symbol +"&fid_input_date_1=" + beginYmd +"&fid_input_date_2=" +endYmd +"&fid_period_div_code=" + period + "&fid_org_adj_prc=" + sendRevisePrice;

        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("code:" + response.getResponseCode() +", " + response.getMessage() +", symbol: " + symbol +", beginYmd: " + beginYmd);
        }

        return response.getMessage();
    }

    /**
     * 일별 신용 정보
     * @param symbol 종복코드
     * @param ymd 결제일자
     * @return 결과값 json
     */
    public String getDailyCreditLoanJson(String symbol, String ymd){

        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/daily-credit-balance";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHPST04760000");

        String query = "?fid_cond_mrkt_div_code=J&fid_cond_scr_div_code=20476&fid_input_iscd=" + symbol +"&fid_input_date_1=" +ymd ;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }
        return response.getMessage();
    }


    public StockCreditLoanDaily[] getCreditLoanDailies(String symbol, String beginYmd, String endYmd){

        List<StockCreditLoanDaily> list = new ArrayList<>();

        String nextBeginYmd = beginYmd;

        int endYmdNum = Integer.parseInt(endYmd);
        String dateFormat = "yyyyMMdd hh:mm";
        outer:
        for(;;){

            int beginYmdNum = Integer.parseInt(nextBeginYmd);

            String callYmd = YmdUtil.getYmd(nextBeginYmd, 30);


            if(YmdUtil.compare(callYmd, endYmd) > 0){
                callYmd = endYmd;
            }

            String jsonText = getDailyCreditLoanJson(symbol, callYmd);

            JSONObject object = new JSONObject(jsonText);
            String code = object.getString("rt_cd");
            if(!code.equals("0")){
                if(!object.isNull("msg1")){
                    throw new StockApiException("rt_cd: " + code + ", message: " + object.getString("msg1"));
                }else{
                    throw new StockApiException("rt_cd: " + code);
                }
            }

            JSONArray array = object.getJSONArray("output");
            int length = array.length();
            for (int i = length -1; i > -1 ; i--) {

                StockCreditLoanDaily creditLoanDaily = new StockCreditLoanDaily();

                JSONObject row = array.getJSONObject(i);

                String tradeYmd = row.getString("deal_date");

                int tradeYmdInt = Integer.parseInt(tradeYmd);

                if(tradeYmdInt < beginYmdNum){
                    continue;
                }

                if(tradeYmdInt > endYmdNum){
                    break outer;
                }

                creditLoanDaily.setTime(Times.getTime(dateFormat, tradeYmd +" 09:00", TradingTimes.KOR_ZONE_ID));

                creditLoanDaily.setTradeYmd(tradeYmdInt);
                creditLoanDaily.setPaymentYmd(Integer.parseInt(row.getString("stlm_date")));

                creditLoanDaily.setLoanNewQuantity(new BigDecimal(row.getString("whol_loan_new_stcn")));
                creditLoanDaily.setLoanRepaymentQuantity(new BigDecimal(row.getString("whol_loan_rdmp_stcn")));
                creditLoanDaily.setLoanBalanceQuantity(new BigDecimal(row.getString("whol_loan_rmnd_stcn")));

                creditLoanDaily.setLoanNewAmount(new BigDecimal(row.getString("whol_loan_new_amt")));
                creditLoanDaily.setLoanRepaymentAmount(new BigDecimal(row.getString("whol_loan_rdmp_amt")));
                creditLoanDaily.setLoanBalanceAmount(new BigDecimal(row.getString("whol_loan_rmnd_amt")));

                creditLoanDaily.setLoanBalanceRate(new BigDecimal(row.getString("whol_loan_rmnd_rate")));
                creditLoanDaily.setLoanTradeRate(new BigDecimal(row.getString("whol_loan_gvrt")));

                creditLoanDaily.setClose(new BigDecimal(row.getString("stck_prpr")));
                creditLoanDaily.setOpen(new BigDecimal(row.getString("stck_oprc")));
                creditLoanDaily.setHigh(new BigDecimal(row.getString("stck_hgpr")));
                creditLoanDaily.setLow(new BigDecimal(row.getString("stck_lwpr")));
                creditLoanDaily.setVolume(new BigDecimal(row.getString("acml_vol")));

                list.add(creditLoanDaily);

            }

            if(YmdUtil.compare(callYmd, endYmd) >= 0){
                break;
            }

            nextBeginYmd = YmdUtil.getYmd(callYmd,1);

            koreainvestmentApi.periodSleep();

        }
        if(list.size() == 0){
            return StockCreditLoanDaily.EMPTY_DAILY_ARRAY;
        }



        return list.toArray(new StockCreditLoanDaily[0]);
    }

    public TradeCandle [] getNxtCandles(String symbol, String period, String beginYmd, String endYmd, boolean isRevisePrice){
        String jsonText = getPeriodDataJsonText(symbol, "NX",period, beginYmd, endYmd, isRevisePrice);
        return getCandles(jsonText);
    }


    public TradeCandle [] getCandles(String symbol, String period, String beginYmd, String endYmd, boolean isRevisePrice){
        String jsonText = getPeriodDataJsonText(symbol, "J",period, beginYmd, endYmd, isRevisePrice);
        return getCandles(jsonText);
    }



    public static TradeCandle [] getCandles(String jsonText){

        JSONObject object = new JSONObject(jsonText);
        String code = object.getString("rt_cd");
        if(!code.equals("0")){
            if(!object.isNull("msg1")){
                throw new StockApiException("rt_cd: " + code + ", message: " + object.getString("msg1"));
            }else{
                throw new StockApiException("rt_cd: " + code);
            }
        }

        JSONArray array = object.getJSONArray("output2");

//        String dateFormat = "yyyyMMdd hh:mm";

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
            tradeCandle.setOpenTime(TradingTimes.getDailyOpenTime(CountryCode.KOR, ymd));
            tradeCandle.setCloseTime(TradingTimes.getDailyCloseTime(CountryCode.KOR, ymd));
            tradeCandle.setOpen(new BigDecimal(row.getString("stck_oprc")));
            tradeCandle.setHigh(new BigDecimal(row.getString("stck_hgpr")));
            tradeCandle.setLow(new BigDecimal(row.getString("stck_lwpr")));
            tradeCandle.setClose(new BigDecimal(row.getString("stck_clpr")));
            tradeCandle.setVolume(new BigDecimal(row.getString("acml_vol")));
            tradeCandle.setAmount(new BigDecimal(row.getString("acml_tr_pbmn")));
            tradeCandle.setChange(new BigDecimal(row.getString("prdy_vrss")));

            //락 유형
            if(!row.isNull("flng_cls_code")) {
                String clsCode = row.getString("flng_cls_code");
                if(!clsCode.equals("00")){
                    tradeCandle.addData(TradeCandleDataKey.LOCK_CODE, clsCode);

//                     * 01 : 권리락
//                     * 02 : 배당락
//                     * 03 : 분배락
//                     * 04 : 권배락
//                     * 05 : 중간(분기)배당락
//                     * 06 : 권리중간배당락
//                     * 07 : 권리분기배당락

                    if(clsCode.equals("01")){
                        tradeCandle.addData(TradeCandleDataKey.LOCK_TYPE, LockType.RIGHTS_LOCK.toString());
                    }else if(clsCode.equals("02")){
                        tradeCandle.addData(TradeCandleDataKey.LOCK_TYPE, LockType.DIVIDEND_LOCK.toString());
                    }else if(clsCode.equals("03")){
                        tradeCandle.addData(TradeCandleDataKey.LOCK_TYPE, LockType.DISTRIBUTION_LOCK.toString());
                    }else if(clsCode.equals("04")){
                        tradeCandle.addData(TradeCandleDataKey.LOCK_TYPE, LockType.RIGHTS_DIVIDEND_LOCK.toString());
                    }else if(clsCode.equals("05")){
                        tradeCandle.addData(TradeCandleDataKey.LOCK_TYPE, LockType.DIVIDEND_LOCK.toString());
                    }else if(clsCode.equals("06")){
                        tradeCandle.addData(TradeCandleDataKey.LOCK_TYPE, LockType.RIGHTS_DIVIDEND_LOCK.toString());
                    }else if(clsCode.equals("07")){
                        tradeCandle.addData(TradeCandleDataKey.LOCK_TYPE, LockType.RIGHTS_DIVIDEND_LOCK.toString());
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

    public String getProgramTradingDataDailyJsonText(String symbol, String ymd){
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/program-trade-by-stock-daily";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHPPG04650200");

        String query = "?fid_cond_mrkt_div_code=J&fid_input_iscd=" + symbol +"&fid_input_date_1=" +ymd ;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }
        return response.getMessage();
    }


    public ProgramDaily[] getProgramDailies(String symbol, String beginYmd, String endYmd){
        List<ProgramDaily> list = new ArrayList<>();


        String nextBeginYmd = beginYmd;

        int endYmdNum = Integer.parseInt(endYmd);
//        String dateFormat = "yyyyMMdd hh:mm";
        outer:
        for(;;) {

            int beginYmdNum = Integer.parseInt(nextBeginYmd);

            String callYmd = YmdUtil.getYmd(nextBeginYmd, 30);
            if (YmdUtil.compare(callYmd, endYmd) > 0) {
                callYmd = endYmd;
            }


            String jsonText = getProgramTradingDataDailyJsonText(symbol, callYmd);

            JSONObject object = new JSONObject(jsonText);
            String code = object.getString("rt_cd");
            if (!code.equals("0")) {
                if (!object.isNull("msg1")) {
                    throw new StockApiException("rt_cd: " + code + ", message: " + object.getString("msg1"));
                } else {
                    throw new StockApiException("rt_cd: " + code);
                }
            }

            JSONArray array = object.getJSONArray("output");
            int length = array.length();
            for (int i = length - 1; i > -1; i--) {
                JSONObject row = array.getJSONObject(i);

                String tradeYmd = row.getString("stck_bsop_date");

                int tradeYmdInt = Integer.parseInt(tradeYmd);

                if(tradeYmdInt < beginYmdNum){
                    continue;
                }

                if(tradeYmdInt > endYmdNum){
                    break outer;
                }

                ProgramDaily daily = new ProgramDaily();

                daily.setTime(TradingTimes.getDailyOpenTime(CountryCode.KOR, tradeYmd));
                daily.setYmd(tradeYmdInt);
                daily.setClose(row.getBigDecimal("stck_clpr"));
                daily.setVolume(row.getBigDecimal("acml_vol"));
                daily.setAmount(row.getBigDecimal("acml_tr_pbmn"));
                daily.setChange(row.getBigDecimal("prdy_vrss"));

                daily.setSellVolume(row.getBigDecimal("whol_smtn_seln_vol"));
                daily.setSellAmount(row.getBigDecimal("whol_smtn_seln_tr_pbmn"));

                daily.setBuyVolume(row.getBigDecimal("whol_smtn_shnu_vol"));
                daily.setBuyAmount(row.getBigDecimal("whol_smtn_shnu_tr_pbmn"));

                daily.setNetBuyVolume(row.getBigDecimal("whol_smtn_ntby_qty"));
                daily.setNetBuyAmount(row.getBigDecimal("whol_smtn_ntby_tr_pbmn"));

                daily.setNetBuyChangeVolume(row.getBigDecimal("whol_ntby_vol_icdc"));
                daily.setNetBuyChangeAmount(row.getBigDecimal("whol_ntby_tr_pbmn_icdc2"));
                list.add(daily);

            }

            if(YmdUtil.compare(callYmd, endYmd) >= 0){
                break;
            }

            nextBeginYmd = YmdUtil.getYmd(callYmd,1);

            koreainvestmentApi.periodSleep();
        }

        if(list.size() == 0){
            return ProgramDaily.EMPTY_ARRAY;
        }
        return list.toArray(new ProgramDaily[0]);
    }

    public String getVolumePowerDailyJsonText(String symbol, String beginYmd, String endYmd){
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/inquire-daily-trade-volume";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHKST03010800");

        String query = "?fid_cond_mrkt_div_code=J&fid_input_iscd=" + symbol +"&fid_input_date_1=" +beginYmd +"&fid_input_date_2=" + endYmd +"&fid_period_div_code=D";
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }
        return response.getMessage();

    }

    //최대 100건
    public VolumePowerDaily []  getVolumePowerDailies(String symbol, String beginYmd, String endYmd){
        String jsonText = getVolumePowerDailyJsonText(symbol, beginYmd, endYmd);
        JSONObject object = new JSONObject(jsonText);

        JSONArray array = object.getJSONArray("output2");
        int length = array.length();
        String dateFormat = "yyyyMMdd hh:mm";


        List<VolumePowerDaily> list = new ArrayList<>();


        for (int i = length -1; i > -1 ; i--) {

            JSONObject row = array.getJSONObject(i);

            if(row.isNull("stck_bsop_date")){
                //상장 이전데이터를 조회할경우
                return VolumePowerDaily.EMPTY_ARRAY;
            }

            VolumePowerDaily daily = new VolumePowerDaily();
            String ymd = row.getString("stck_bsop_date");

            if(ymd.equals("0")){
                continue;
            }

            if(ymd.length() != 8){
                continue;
            }

            daily.setYmd(Integer.parseInt(ymd));
            daily.setTime(Times.getTime(dateFormat, ymd +" 09:00", TradingTimes.KOR_ZONE_ID));
            daily.setSellVolume(row.getBigDecimal("total_seln_qty"));
            daily.setBuyVolume(row.getBigDecimal("total_shnu_qty"));

            list.add(daily);

        }
        return list.toArray(new VolumePowerDaily[0]);
    }


    public String get1mCandleJsonText(String symbol, String exchange, String ymd, String hm){
        //apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock-quotations2#L_9fece97b-401f-4379-9e9d-4365b63c1126
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/inquire-time-dailychartprice";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHKST03010230");

        if(exchange.equals("KRX")){
            exchange = "J";
        }else if(exchange.equals("NXT")){
            exchange = "NX";
        }

        String query = "?FID_COND_MRKT_DIV_CODE=" + exchange +"&FID_INPUT_ISCD=" + symbol +"&FID_INPUT_DATE_1=" +ymd +"&FID_INPUT_HOUR_1=" + hm +"00&FID_PW_DATA_INCU_YN=Y&FID_FAKE_TICK_INCU_YN=N";
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }
        return response.getMessage();
    }

    public TradeCandle [] get1mCandles(String symbol, String exchange, String ymd){

//        List<String>
        //23시를 기록하여 마지막 시간값을 가져온다.
        String firstText = get1mCandleJsonText(symbol,exchange, ymd, "2300");
        JSONObject object = new JSONObject(firstText);
        String code = object.getString("rt_cd");
        if(!code.equals("0")){
            if(!object.isNull("msg1")){
                throw new StockApiException("rt_cd: " + code + ", message: " + object.getString("msg1"));
            }else{
                throw new StockApiException("rt_cd: " + code);
            }
        }
        if(object.isNull("output2")){
            return TradeCandle.EMPTY_CANDLES;
        }

        if(object.isNull("output1")){
            return TradeCandle.EMPTY_CANDLES;
        }

        JSONArray array = object.getJSONArray("output2");
        if(array.isEmpty()){
            return TradeCandle.EMPTY_CANDLES;
        }

        BigDecimal previous =  object.getJSONObject("output1").getBigDecimal("stck_prdy_clpr");

        boolean isBreak = false;
        List<TradeCandle> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            TradeCandle candle = get1mCandle(array.getJSONObject(i),ymd);
            if(candle == null){
                isBreak = true;
                break;
            }
            list.add(candle);
        }

        for(;;){
            if(isBreak){
                break;
            }
            koreainvestmentApi.minuteSleep();

            TradeCandle last = list.get(list.size()-1);
            String ymdhm = Times.ymdhm(last.getOpenTime(), TradingTimes.KOR_ZONE_ID);
            String next = Times.getYmdhm(ymdhm, -Times.MINUTE_1);
            next = Times.getHm(next);
            String jsonText = get1mCandleJsonText(symbol, exchange, ymd, next);

            object = new JSONObject(jsonText);
            code = object.getString("rt_cd");
            if(!code.equals("0")){
                if(!object.isNull("msg1")){
                    throw new StockApiException("rt_cd: " + code + ", message: " + object.getString("msg1"));
                }else{
                    throw new StockApiException("rt_cd: " + code);
                }
            }

            if(object.isNull("output2")){
                break;
            }

            array = object.getJSONArray("output2");
            if(array.isEmpty()){
                break;
            }

            for (int i = 0; i < array.length(); i++) {
                TradeCandle candle = get1mCandle(array.getJSONObject(i),ymd);
                if(candle == null){
                    isBreak = true;
                    break;
                }
                list.add(candle);
            }
        }

        if(list.isEmpty()){
            return TradeCandle.EMPTY_CANDLES;
        }

        TradeCandle [] candles = new TradeCandle[list.size()];
        TradeCandle firstCandle =  list.get(list.size()-1);
        firstCandle.setPrevious(previous);
        firstCandle.setChange();
        firstCandle.setEndTrade();
        int index = 0;
        candles[index++] = firstCandle;

        for (int i = candles.length-2; i >-1 ; i--) {
            TradeCandle candle = list.get(i);
            candle.setPrevious(list.get(i+1).getPrevious());
            candle.setChange();
            candle.setEndTrade();
            candles[index++] = candle;
        }

        return candles;
    }

    public TradeCandle get1mCandle(JSONObject row, String inYmd){

        String ymd = row.getString("stck_bsop_date");
        String hms = row.getString("stck_cntg_hour");

        if(inYmd != null && !ymd.equals(inYmd)){
            return null;
        }

        long openTime = Times.getTime("yyyyMMdd HHmmss", ymd +" " + hms, TradingTimes.KOR_ZONE_ID);
        long closeTime = openTime + Times.MINUTE_1;

        TradeCandle tradeCandle = new TradeCandle();
        tradeCandle.setOpenTime(openTime);
        tradeCandle.setCloseTime(closeTime);
        tradeCandle.setOpen(row.getBigDecimal("stck_oprc"));
        tradeCandle.setHigh(row.getBigDecimal("stck_hgpr"));
        tradeCandle.setLow(row.getBigDecimal("stck_lwpr"));
        tradeCandle.setClose(row.getBigDecimal("stck_prpr"));
        tradeCandle.setVolume(row.getBigDecimal("cntg_vol"));

        tradeCandle.addData(TradeCandleDataKey.SUM_AMOUNT, row.getString("acml_tr_pbmn"));
        tradeCandle.setEndTrade();

        return tradeCandle;
    }

    public static void main(String[] args) {

        String ymdhm = Times.ymdhm(System.currentTimeMillis(), TradingTimes.KOR_ZONE_ID);
        System.out.println(ymdhm);

        System.out.println(Times.getYmdhm(ymdhm, -Times.MINUTE_5));
    }

}