package io.runon.stock.securities.firm.api.kor.ls;

import io.runon.commons.apis.http.HttpApiResponse;
import io.runon.stock.trading.data.daily.StockInvestorDaily;
import io.runon.stock.trading.exception.StockApiException;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ls증권 open api
 * @author macle
 */
public class LsPeriodDataApi {

    private final LsApi lsApi;
    public LsPeriodDataApi(LsApi lsApi){
        this.lsApi = lsApi;
    }

    public String getInvestorDailyJson(String symbol, String beginYmd, String endYmd
    ){
        return getInvestorDailyJson(symbol, beginYmd, endYmd, 0);
    }

    //최대 250건
    public String getInvestorDailyJson(String symbol, String beginYmd, String endYmd, int reTryCount){
        lsApi.updateAccessToken();
        String url = "/stock/frgr-itt";
        Map<String, String> requestHeaderMap = lsApi.computeIfAbsenttTrCodeMap("t1717");

        JSONObject paramObject = new JSONObject();
        paramObject.put("shcode", symbol);
        paramObject.put("gubun", "0");
        paramObject.put("fromdt", beginYmd);
        paramObject.put("todt", endYmd);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("t1717InBlock", paramObject);

        HttpApiResponse response =  lsApi.getHttpPost().getResponse(url , requestHeaderMap, jsonObject.toString());

        if(response.getResponseCode() != 200){
            if(reTryCount < 1 && lsApi.isAccessTokenUpdate(response)){
                return getInvestorDailyJson(symbol, beginYmd, endYmd, reTryCount+1);
                //토큰 에러일경우 재시도 LS증권 api에서 토큰애러가 나타나는 문제 발견
            }

            throw new StockApiException("fail code:" + response.getResponseCode() +", " + response.getMessage() +", stock symbol: " + symbol+", beginYmd: " + beginYmd + ", endYmd: " + endYmd);
        }

        return response.getMessage();
    }

    public StockInvestorDaily[] getInvestorDailies(String symbol, String beginYmd, String endYmd){

        String jsonText = getInvestorDailyJson(symbol, beginYmd, endYmd);

        JSONObject jsonObject = new JSONObject(jsonText);

        JSONArray array = jsonObject.getJSONArray("t1717OutBlock");


        int length = array.length();


        List<StockInvestorDaily> list = new ArrayList<>();

        for (int i = length -1; i > -1 ; i--) {
            JSONObject row = array.getJSONObject(i);

            StockInvestorDaily daily = new StockInvestorDaily();

            String date = row.getString("date");
            if(date.isEmpty()){
               continue;
            }

            daily.setYmd(Integer.parseInt(date));
            daily.setClose(row.getBigDecimal("close"));
            daily.setChange(row.getBigDecimal("change"));

            //사모펀트
            daily.setPrivateFund(row.getBigDecimal("tjj0000_vol"));
            daily.setPensionFundPrice(row.getBigDecimal("tjj0000_dan"));

            //증권
            daily.setSecurities(row.getBigDecimal("tjj0001_vol"));
            daily.setSecuritiesPrice(row.getBigDecimal("tjj0001_dan"));

            //보험
            daily.setInsurance(row.getBigDecimal("tjj0002_vol"));
            daily.setInsurancePrice((row.getBigDecimal("tjj0002_dan")));

            //투자신탁(투신)
            daily.setInvestmentTrust(row.getBigDecimal("tjj0003_vol"));
            daily.setInvestmentTrustPrice(row.getBigDecimal("tjj0003_dan"));

            //은행
            daily.setBank(row.getBigDecimal("tjj0004_vol"));
            daily.setBankPrice(row.getBigDecimal("tjj0004_dan"));

            //종합금융회사 (종금)
            daily.setGeneralFinance(row.getBigDecimal("tjj0005_vol"));
            daily.setGeneralFinancePrice(row.getBigDecimal("tjj0005_dan"));

            //기금 (연기금)
            daily.setPensionFund(row.getBigDecimal("tjj0006_vol"));
            daily.setPensionFundPrice(row.getBigDecimal("tjj0006_dan"));

            //기타법인
            daily.setOtherCorporation(row.getBigDecimal("tjj0007_vol"));
            daily.setOtherCorporationPrice(row.getBigDecimal("tjj0007_dan"));

            //개인
            daily.setIndividual(row.getBigDecimal("tjj0008_vol"));
            daily.setIndividualPrice(row.getBigDecimal("tjj0008_dan"));

            //등록외국인
            daily.setForeignerReg(row.getBigDecimal("tjj0009_vol"));
            daily.setForeignerRegPrice(row.getBigDecimal("tjj0009_dan"));

            //미등록 외국인
            daily.setForeignerUnReg(row.getBigDecimal("tjj0010_vol"));
            daily.setForeignerUnRegPrice(row.getBigDecimal("tjj0010_dan"));

            //국가외(국가내의 기타를 뜻하는것 같음, 정확한 용어정리가 되면 변경될 여지가 있음)
            daily.setCountryEtc(row.getBigDecimal("tjj0011_vol"));
            daily.setCountryEtcPrice(row.getBigDecimal("tjj0011_dan"));

//            //기관
            daily.setInstitution(row.getBigDecimal("tjj0018_vol"));
            daily.setInstitutionPrice(row.getBigDecimal("tjj0018_dan"));

//            //dhl국계
            daily.setForeign(row.getBigDecimal("tjj0016_vol"));
            daily.setForeignPrice(row.getBigDecimal("tjj0016_dan"));
//
//            //기타계 (기타 + 국가)
            daily.setEtc(row.getBigDecimal("tjj0017_vol"));
            daily.setEtcPrice(row.getBigDecimal("tjj0017_dan"));

            list.add(daily);

        }
        return list.toArray(new StockInvestorDaily[0]);
    }

}
