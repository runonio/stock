package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.http.HttpApiResponse;
import io.runon.stock.trading.exception.StockApiException;

import java.util.Map;

/**
 * 한국투자증권 선물관련 api
 * API가 많아서 정리한 클래스를 나눈다.
 * @author macle
 */
public class KoreainvestmentFuturesApi {

    private final KoreainvestmentApi koreainvestmentApi;
    public KoreainvestmentFuturesApi(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;
    }

    /**
     * reviewjang.tistory.com/123
     * 선물옵션 코드 체게 이해해야함.
     *
     * @param marketDivCode    F: 지수선물, O:지수옵션
     * JF: 주식선물, JO:주식옵션,
     * CF: 상품선물(금), 금리선물(국채), 통화선물(달러)
     * CM: 야간선물, EU: 야간옵션
     * @param symbol 종목번호 (지수선물:6자리, 지수옵션 9자리)
     * @param period D:일봉 W:주봉, M:월봉, Y:년봉
     * @param beginYmd 	조회 시작일자 (ex. 20220401)
     * @param endYmd 조회 종료일자 (ex. 20220524)
     * @return json object
     */
    public String getPeriodFuturesJsonText(String marketDivCode, String symbol ,String period, String beginYmd, String endYmd){
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-futureoption/v1/quotations/inquire-daily-fuopchartprice";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHKIF03020100");

        String query = "?fid_cond_mrkt_div_code=" + marketDivCode + "&fid_input_iscd=" + symbol +"&fid_input_date_1=" + beginYmd +"&fid_input_date_2=" +endYmd +"&fid_period_div_code=" + period ;


        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("code:" + response.getResponseCode() +", " + response.getMessage() +", symbol: " + symbol +", beginYmd: " + beginYmd);
        }

        return response.getMessage();

    }




    public static void main(String[] args) {

    }
}
