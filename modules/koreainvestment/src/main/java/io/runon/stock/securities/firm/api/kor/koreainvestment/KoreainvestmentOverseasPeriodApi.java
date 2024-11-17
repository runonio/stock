package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.http.HttpApiResponse;
import io.runon.stock.trading.exception.StockApiException;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 한국 투자증권 해외주식 기간별 시세 관련 api 정리
 * @author macle
 */
public class KoreainvestmentOverseasPeriodApi {
    private final KoreainvestmentApi koreainvestmentApi;
    public KoreainvestmentOverseasPeriodApi(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;
    }


    public String get1mCandleJsonText(String exchange, String symbol, String ymd, String hm){
        //apiportal.koreainvestment.com/apiservice/apiservice-oversea-stock-quotations#L_852d7e45-4f34-418b-b6a1-a4552bbcdf90
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/overseas-price/v1/quotations/inquire-time-itemchartprice";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","HHDFS76950200");

        String exchangeCode = exchange;
        if(exchangeCode.equals("NASDAQ")){
            exchangeCode = "NAS";
        }else if(exchangeCode.equals("NYSE")){
            exchangeCode = "NYS";
        }else if(exchangeCode.equals("AMEX")){
            exchangeCode = "AMS";
        }

        String query = "?AUTH=&EXCD=" + exchangeCode + "&SYMB=" + symbol + "&NMIN=1&PINC=1&NEXT=1&NREC=120&FILL=&KEYB=" +ymd+hm+"00";
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();
    }

    public String getDailyJsonText(String exchange, String symbol, String baseYmd, boolean isRevisePrice){
        //apiportal.koreainvestment.com/apiservice/apiservice-oversea-stock-quotations#L_0e9fb2ba-bbac-4735-925a-a35e08c9a790

        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/overseas-price/v1/quotations/dailyprice";

        String exchangeCode = exchange;
        if(exchangeCode.equals("NASDAQ")){
            exchangeCode = "NAS";
        }else if(exchangeCode.equals("NYSE")){
            exchangeCode = "NYS";
        }else if(exchangeCode.equals("AMEX")){
            exchangeCode = "AMS";
        }

        String modp;
        if(isRevisePrice){
            modp = "1";
        }else{
            modp = "0";
        }

        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","HHDFS76240000");
        String query = "?AUTH=&EXCD=" + exchangeCode + "&SYMB=" + symbol + "&GUBN=0&BYMD=" + baseYmd+"&MODP="+ modp;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();
    }


    public TradeCandle [] getDailyCandles(CountryCode countryCode, String jsonText){
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
        if(array.length() == 0){
            return TradeCandle.EMPTY_CANDLES;
        }

        int length = array.length();

        int candleIndex = 0;
        TradeCandle [] candles = new TradeCandle[length];
        for (int i = length -1; i > -1 ; i--) {
            JSONObject row = array.getJSONObject(i);

            TradeCandle tradeCandle = new TradeCandle();

            String ymd = row.getString("xymd");


            tradeCandle.setOpenTime(TradingTimes.getDailyOpenTime(countryCode, ymd));
            tradeCandle.setCloseTime(TradingTimes.getDailyCloseTime(countryCode, ymd));
            tradeCandle.setOpen(new BigDecimal(row.getString("open")));
            tradeCandle.setHigh(new BigDecimal(row.getString("high")));
            tradeCandle.setLow(new BigDecimal(row.getString("low")));
            tradeCandle.setClose(new BigDecimal(row.getString("clos")));
            tradeCandle.setVolume(new BigDecimal(row.getString("tvol")));
            tradeCandle.setAmount(new BigDecimal(row.getString("tamt")));
            tradeCandle.setChange(new BigDecimal(row.getString("diff")));

            tradeCandle.setChange();
            tradeCandle.setEndTrade();
            candles[candleIndex++] = tradeCandle;
        }

        return candles;
    }


}
