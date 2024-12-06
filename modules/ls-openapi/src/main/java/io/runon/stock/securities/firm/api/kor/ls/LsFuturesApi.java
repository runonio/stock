package io.runon.stock.securities.firm.api.kor.ls;

import io.runon.commons.http.HttpApiResponse;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.stock.trading.exception.StockApiException;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.TradeCandleDataKey;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author macle
 */
public class LsFuturesApi {
    private final LsApi lsApi;

    public LsFuturesApi(LsApi lsApi){
        this.lsApi = lsApi;
    }
    public String getDailyFuturesJsonText(String symbol, String beginYmd, String endYmd){
        return getDailyFuturesJsonText(symbol, beginYmd, endYmd, 0);
    }

    public String getDailyFuturesJsonText(String symbol, String beginYmd, String endYmd, int reTryCount){
        lsApi.updateAccessToken();

        String url = "/futureoption/chart";

        Map<String, String> requestHeaderMap = lsApi.computeIfAbsenttTrCodeMap("t8416");

        JSONObject paramObject = new JSONObject();
        paramObject.put("shcode", symbol);
        paramObject.put("sdate", beginYmd);
        paramObject.put("edate", endYmd);
        paramObject.put("cts_date", " ");
        paramObject.put("comp_yn", "N");
        paramObject.put("qrycnt", 500);
        paramObject.put("gubun", "2");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t8416InBlock", paramObject);

        HttpApiResponse response =  lsApi.getHttpPost().getResponse(url , requestHeaderMap, jsonObject.toString());


        if(response.getResponseCode() != 200){
            if(reTryCount < 1 && lsApi.isAccessTokenUpdate(response)){
                return getDailyFuturesJsonText(symbol, beginYmd, endYmd, reTryCount+1);
                //토큰 에러일경우 재시도 LS증권 api에서 토큰애러가 나타나는 문제 발견
            }

            throw new StockApiException("fail code:" + response.getResponseCode() +", " + response.getMessage() +", symbol: " + symbol+", beginYmd: " + beginYmd + ", endYmd: " + endYmd);
        }

        return response.getMessage();
    }


    public TradeCandle [] getDailyCandles(String symbol, String beginYmd, String endYmd){


        List<TradeCandle> list = new ArrayList<>();


        String searchBeginYmd = beginYmd;
        for(;;){

            String searchEndYmd = YmdUtil.getYmd(searchBeginYmd, 450);

            if(YmdUtil.compare(searchEndYmd, endYmd) > 0){
                 searchEndYmd = endYmd;
            }

            String jsonText = getDailyFuturesJsonText(symbol, searchBeginYmd, searchEndYmd);


            JSONObject jsonObject = new JSONObject(jsonText);

            if(jsonObject.isNull("t8416OutBlock1")){
                if(YmdUtil.compare(searchEndYmd, endYmd) >=0) {
                    break;
                }

                searchBeginYmd = YmdUtil.getYmd(searchEndYmd,1);
                lsApi.periodSleep();
                continue;
            }

            JSONArray array = jsonObject.getJSONArray("t8416OutBlock1");

            for (int i = 0; i <array.length() ; i++) {
                JSONObject row = array.getJSONObject(i);
                BigDecimal volume = row.getBigDecimal("jdiff_vol");
                if(volume.compareTo(BigDecimal.ZERO) == 0){
                    continue;
                }


                String ymd = row.getString("date");


                TradeCandle tradeCandle = new TradeCandle();
                tradeCandle.setOpenTime(TradingTimes.getDailyOpenTime(CountryCode.KOR, ymd));
                tradeCandle.setCloseTime(TradingTimes.getDailyCloseTime(CountryCode.KOR, ymd));

                tradeCandle.setOpen(row.getBigDecimal("open"));
                tradeCandle.setClose(row.getBigDecimal("close"));
                tradeCandle.setHigh(row.getBigDecimal("high"));
                tradeCandle.setLow(row.getBigDecimal("low"));

                tradeCandle.setVolume(volume);
                tradeCandle.setAmount(row.getBigDecimal("value"));

                tradeCandle.addData(TradeCandleDataKey.OPEN_INTEREST,row.getBigDecimal("openyak").stripTrailingZeros().toPlainString());

                list.add(tradeCandle);
            }

            if(YmdUtil.compare(searchEndYmd, endYmd) >=0) {
                break;
            }

            searchBeginYmd = YmdUtil.getYmd(searchEndYmd,1);
            lsApi.periodSleep();
        }

        return list.toArray(new TradeCandle[0]);
    }

}
