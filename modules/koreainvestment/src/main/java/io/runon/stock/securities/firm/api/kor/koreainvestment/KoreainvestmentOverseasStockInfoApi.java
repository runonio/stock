package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.commons.apis.http.HttpApiResponse;
import io.runon.stock.securities.firm.api.kor.koreainvestment.jdbc.OverseasStockUpdateInfo;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.exception.StockApiException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 해외주식 기본정보 관련
 * @author macle
 */
public class KoreainvestmentOverseasStockInfoApi {
    private final KoreainvestmentApi koreainvestmentApi;
    public KoreainvestmentOverseasStockInfoApi(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;
    }

    /**
     *
     * @param exchangeCode
     * 512 미국 나스닥 / 513 미국 뉴욕 / 529 미국 아멕스
     * 515 일본
     * 501 홍콩 / 543 홍콩CNY / 558 홍콩USD
     * 507 베트남 하노이 / 508 베트남 호치민
     * 551 중국 상해A / 552 중국 심천A
     */
    public String getStockInfoJsonText(String exchangeCode, String symbol){
        //apiportal.koreainvestment.com/apiservice/apiservice-oversea-stock-quotations#L_7f77a12b-b23c-4605-93ea-4e1b3c0356fb
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/overseas-price/v1/quotations/search-info";
        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id","CTPF1702R");
        String query = "?PRDT_TYPE_CD=" + exchangeCode + "&PDNO=" + symbol;
        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();

    }

    public String getStockInfoJsonText(Stock stock){
        String market = stock.getMarket();
        if(market == null){
            throw new StockApiException("market null");
        }

        String exchangeCode = null;

        if(market.equals("NASDAQ")){
            exchangeCode = "512";
        }else if(market.equals("NYSE")){
            exchangeCode = "513";
        }else if(market.equals("AMEX")){
            exchangeCode = "529";
        }

        if(exchangeCode == null){
            throw new StockApiException("market not mapping: " + market);
        }


        return getStockInfoJsonText(exchangeCode, stock.getSymbol());

    }

    /**
     * 업데이트 정보생성
     */
    public OverseasStockUpdateInfo getStockInfo(Stock stock){
        String jsonText = getStockInfoJsonText(stock);

        JSONObject object = new JSONObject(jsonText);
        object = object.getJSONObject("output");
        OverseasStockUpdateInfo info = new OverseasStockUpdateInfo();
        info.setStockId( stock.getStockId());
        if (!object.isNull("istt_usge_isin_cd")) {
            info.setIsin(object.getString("istt_usge_isin_cd"));
        }
        info.setIsListing(object.getString("lstg_yn").equals("Y"));
        info.setIssuedShares(Long.parseLong(object.getString("lstg_stck_num")));

        String listedYmd = object.getString("lstg_dt").trim();
        if(listedYmd.length() == 8 ){

            int ymd = Integer.parseInt(listedYmd);
            if(ymd > 0) {
                info.setListedYmd(ymd);
            }
        }

        String deListedYmd = object.getString("lstg_abol_dt").trim();
        if(deListedYmd.length() == 8){
            int ymd = Integer.parseInt(deListedYmd);
            if(ymd > 0) {
                info.setDelistedYmd(ymd);
            }
        }

        return info;
    }

}
