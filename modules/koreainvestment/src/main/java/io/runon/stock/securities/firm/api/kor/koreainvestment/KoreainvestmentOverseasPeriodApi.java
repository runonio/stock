package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.http.HttpApiResponse;
import io.runon.stock.trading.exception.StockApiException;

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


}
