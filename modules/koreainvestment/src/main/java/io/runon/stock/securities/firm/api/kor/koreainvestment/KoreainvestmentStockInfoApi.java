package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.http.HttpApiResponse;
import io.runon.stock.trading.exception.StockApiException;

import java.util.Map;

/**
 * 한국 주식 기본정보, 재무제표 관련 api 정리
 * @author macle
 */
public class KoreainvestmentStockInfoApi {
    private final KoreainvestmentApi koreainvestmentApi;
    public KoreainvestmentStockInfoApi(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;
    }



    public String getStockInfoJsonText(String symbol){
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/search-stock-info";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","CTPF1002R");
        String query = "?PRDT_TYPE_CD=300&PDNO=" + symbol;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();

    }

    //대차대조표
//    https://apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock-information#L_f77aedcb-b46f-4aa0-b062-f03b9a444405

    /**
     * 대차대조표
     *  apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock-information#L_f77aedcb-b46f-4aa0-b062-f03b9a444405
     *  -stac_yymm	결산 년월	String	Y	6
     * -cras	유동자산	String	Y	112
     * -fxas	고정자산	String	Y	112
     * -total_aset	자산총계	String	Y	102
     * -flow_lblt	유동부채	String	Y	112
     * -fix_lblt	고정부채	String	Y	112
     * -total_lblt	부채총계	String	Y	102
     * -cpfn	자본금	String	Y	22
     * -cfp_surp	자본 잉여금	String	Y	182	출력되지 않는 데이터(99.99 로 표시)
     * -prfi_surp	이익 잉여금	String	Y	182	출력되지 않는 데이터(99.99 로 표시)
     * -total_cptl	자본총계	String	Y	102
     * @param yearQuarter year : 0, quarter : 1
     * @param symbol 종목 기호
     */
    public String getBalanceSheetJsonText(String yearQuarter, String symbol){
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/finance/balance-sheet";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHKST66430100");
        String query = "?FID_DIV_CLS_CODE=" + yearQuarter + "&fid_cond_mrkt_div_code=J&fid_input_iscd=" + symbol;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();
    }



}
