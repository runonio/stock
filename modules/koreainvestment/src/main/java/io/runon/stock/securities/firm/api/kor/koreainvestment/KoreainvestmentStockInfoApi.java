package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.http.HttpApiResponse;
import io.runon.stock.trading.exception.StockApiException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * 한국 주식 기본정보, 재무제표 관련 api 정리
 * @author macle
 */
public class KoreainvestmentStockInfoApi {

    public static final String ESTIMATED_PERFORMANCE_KEY = "koreainvestment_estimated_performance";

    public static final String INVEST_OPINION_KEY = "koreainvestment_invest_opinion";

    public static final String INVEST_OPINION_SECURITIES_KEY = "koreainvestment_invest_opinion_securities";


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


    /**
     * 추정실적얻기
     * @param symbol 종목 기호
     * @return 추정실적 json text
     */
    public String getEstimatedPerformance(String symbol){
        //apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock-information#L_fbb4bb45-57bb-4037-905d-dff0ff635cf6
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/estimate-perform";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","HHKST668300C0");
        String query = "?SHT_CD=" + symbol ;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();

    }


    public boolean isEstimatedPerformance(String jsonText){
        JSONObject jsonObject = new JSONObject(jsonText);
        return isEstimatedPerformance(jsonObject);
    }



    public boolean isEstimatedPerformance(JSONObject object){
        if(object.isNull("output2")){
            return false;
        }

        JSONArray array = object.getJSONArray("output2");
        if(array.length() > 0 ){
            return true;
        }

        return false;
    }

    public String getInvestOpinion(String symbol, String beginYmd, String endYmd){
        //apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock-information#L_9de56f62-938c-40df-970e-8fd13a59b445
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/invest-opinion";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHKST663300C0");
        String query = "?FID_COND_MRKT_DIV_CODE=J&FID_COND_SCR_DIV_CODE=16633&FID_INPUT_ISCD=" + symbol +"&FID_INPUT_DATE_1=" + beginYmd +"&FID_INPUT_DATE_2=" + endYmd ;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();
    }


    public String getInvestOpinionSecurities(String symbol, String beginYmd, String endYmd){
        //apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock-information#L_3a588de4-df48-49ac-88ca-9765998c00e1
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/quotations/invest-opbysec";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","FHKST663400C0");
        String query = "?FID_COND_MRKT_DIV_CODE=J&FID_COND_SCR_DIV_CODE=16633&FID_DIV_CLS_CODE=0&FID_INPUT_ISCD=" + symbol +"&FID_INPUT_DATE_1=" + beginYmd +"&FID_INPUT_DATE_2=" + endYmd ;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }
        return response.getMessage();
    }



}
