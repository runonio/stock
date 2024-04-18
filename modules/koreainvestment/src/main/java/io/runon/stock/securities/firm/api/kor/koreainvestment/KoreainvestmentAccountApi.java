package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.http.HttpApiResponse;
import io.runon.stock.securities.firm.api.kor.koreainvestment.exception.KoreainvestmentApiException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 한국투자증권 계좌관련 API정리
 *  API가 많아서 정리한 클래스를 나눈다.
 * @author macle
 */
public class KoreainvestmentAccountApi {


    private final KoreainvestmentApi koreainvestmentApi;
    public KoreainvestmentAccountApi(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;
    }

    public String getInquireBalanceJsonText(String accountNumberValue ){

        String cano = accountNumberValue.substring(0,8);
        String acnt_prdt_cd = accountNumberValue.substring(accountNumberValue.length()-2 );

        return getInquireBalanceJsonText(cano, acnt_prdt_cd, "","","02","01","","","","","");
    }

    /**
     * apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock#L_66c61080-674f-4c91-a0cc-db5e64e9a5e6
     *
     * requenst
     * CANO	종합계좌번호	String	Y	8	계좌번호 체계(8-2)의 앞 8자리
     * ACNT_PRDT_CD	계좌상품코드	String	Y	2	계좌번호 체계(8-2)의 뒤 2자리
     * AFHR_FLPR_YN	시간외단일가여부	String	Y	1	N : 기본값, Y : 시간외단일가
     *
     * OFL_YN	오프라인여부	String	Y	1	공란(Default)
     * INQR_DVSN	조회구분	String	Y	2	01 : 대출일별 02 : 종목별
     * UNPR_DVSN	단가구분	String	Y	2	01 : 기본값
     * FUND_STTL_ICLD_YN	펀드결제분포함여부	String	Y	1	N : 포함하지 않음
     * Y : 포함
     * FNCG_AMT_AUTO_RDPT_YN	융자금액자동상환여부	String	Y	1	N : 기본값
     * PRCS_DVSN	처리구분	String	Y	2	00 : 전일매매포함
     * 01 : 전일매매미포함
     * CTX_AREA_FK100	연속조회검색조건100	String	Y	100	공란 : 최초 조회시
     * 이전 조회 Output CTX_AREA_FK100 값 : 다음페이지 조회시(2번째부터)
     * CTX_AREA_NK100	연속조회키100	String	Y	100	공란 : 최초 조회시
     * 이전 조회 Output CTX_AREA_NK100 값 : 다음페이지 조회시(2번째부터
     *
     * response
     * rt_cd	성공 실패 여부	String	Y	1	0 : 성공
     * 0 이외의 값 : 실패
     * msg_cd	응답코드	String	Y	8	응답코드
     * msg1	응답메세지	String	Y	80	응답메세지
     * ctx_area_fk100	연속조회검색조건100	String	Y	100
     * ctx_area_nk100	연속조회키100	String	Y	100
     * output1	응답상세1	Array	Y
     * -pdno	상품번호	String	Y	12	종목번호(뒷 6자리)
     * -prdt_name	상품명	String	Y	60	종목명
     * -trad_dvsn_name	매매구분명	String	Y	60	매수매도구분
     * -bfdy_buy_qty	전일매수수량	String	Y	10
     * -bfdy_sll_qty	전일매도수량	String	Y	10
     * -thdt_buyqty	금일매수수량	String	Y	10
     * -thdt_sll_qty	금일매도수량	String	Y	10
     * -hldg_qty	보유수량	String	Y	19
     * -ord_psbl_qty	주문가능수량	String	Y	10
     * -pchs_avg_pric	매입평균가격	String	Y	22	매입금액 / 보유수량
     * -pchs_amt	매입금액	String	Y	19
     * -prpr	현재가	String	Y	19
     * -evlu_amt	평가금액	String	Y	19
     * -evlu_pfls_amt	평가손익금액	String	Y	19	평가금액 - 매입금액
     * -evlu_pfls_rt	평가손익율	String	Y	9
     * -evlu_erng_rt	평가수익율	String	Y	31	미사용항목(0으로 출력)
     * -loan_dt	대출일자	String	Y	8	INQR_DVSN(조회구분)을 01(대출일별)로 설정해야 값이 나옴
     * -loan_amt	대출금액	String	Y	19
     * -stln_slng_chgs	대주매각대금	String	Y	19
     * -expd_dt	만기일자	String	Y	8
     * -fltt_rt	등락율	String	Y	31
     * -bfdy_cprs_icdc	전일대비증감	String	Y	19
     * -item_mgna_rt_name	종목증거금율명	String	Y	20
     * -grta_rt_name	보증금율명	String	Y	20
     * -sbst_pric	대용가격	String	Y	19	증권매매의 위탁보증금으로서 현금 대신에 사용되는 유가증권 가격
     * -stck_loan_unpr	주식대출단가	String	Y	22
     * output2	응답상세2	Array	Y
     * -dnca_tot_amt	예수금총금액	String	Y	19	예수금
     * -nxdy_excc_amt	익일정산금액	String	Y	19	D+1 예수금
     * -prvs_rcdl_excc_amt	가수도정산금액	String	Y	19	D+2 예수금
     * -cma_evlu_amt	CMA평가금액	String	Y	19
     * -bfdy_buy_amt	전일매수금액	String	Y	19
     * -thdt_buy_amt	금일매수금액	String	Y	19
     * -nxdy_auto_rdpt_amt	익일자동상환금액	String	Y	19
     * -bfdy_sll_amt	전일매도금액	String	Y	19
     * -thdt_sll_amt	금일매도금액	String	Y	19
     * -d2_auto_rdpt_amt	D+2자동상환금액	String	Y	19
     * -bfdy_tlex_amt	전일제비용금액	String	Y	19
     * -thdt_tlex_amt	금일제비용금액	String	Y	19
     * -tot_loan_amt	총대출금액	String	Y	19
     * -scts_evlu_amt	유가평가금액	String	Y	19
     * -tot_evlu_amt	총평가금액	String	Y	19	유가증권 평가금액 합계금액 + D+2 예수금
     * -nass_amt	순자산금액	String	Y	19
     * -fncg_gld_auto_rdpt_yn	융자금자동상환여부	String	Y	1	보유현금에 대한 융자금만 차감여부
     * 신용융자 매수체결 시점에서는 융자비율을 매매대금 100%로 계산 하였다가 수도결제일에 보증금에 해당하는 금액을 고객의 현금으로 충당하여 융자금을 감소시키는 업무
     * -pchs_amt_smtl_amt	매입금액합계금액	String	Y	19
     * -evlu_amt_smtl_amt	평가금액합계금액	String	Y	19	유가증권 평가금액 합계금액
     * -evlu_pfls_smtl_amt	평가손익합계금액	String	Y	19
     * -tot_stln_slng_chgs	총대주매각대금	String	Y	19
     * -bfdy_tot_asst_evlu_amt	전일총자산평가금액	String	Y	19
     * -asst_icdc_amt	자산증감액	String	Y	19
     * -asst_icdc_erng_rt	자산증감수익율	String	Y	31	데이터 미제공
     *
     * @param cano 종합계좌번호	String	Y	8	계좌번호 체계(8-2)의 앞 8자리
     * @param acnt_prdt_cd 계좌상품코드	String	Y	2	계좌번호 체계(8-2)의 뒤 2자리
     * @param afhr_flpr_yn  시간외단일가여부	String	Y	1	N : 기본값 Y : 시간외단일가
     * @param ofl_yn 오프라인여부	String	Y	1	공란(Default)
     * @param inqr_dvsn 조회구분	String	Y	2	01 : 대출일별 02 : 종목별
     * @param unpr_dvsn 단가구분	String	Y	2	01 : 기본값
     * @param fund_sttl_icld_yn 펀드결제분포함여부	String	Y	1	N : 포함하지 않음 Y : 포함
     * @param fncg_amt_auto_rdpt_yn 융자금액자동상환여부	String	Y	1	N : 기본값
     * @param prcs_dvsn 처리구분	String	Y	2	00 : 전일매매포함 01 : 전일매매미포함
     * @param ctx_area_fk100 연속조회검색조건100	String	Y	100	공란 : 최초 조회시 이전 조회 Output CTX_AREA_FK100 값 : 다음페이지 조회시(2번째부터)
     * @param ctx_area_nk100 연속조회키100	String	Y	100	공란 : 최초 조회시
     * @return json text
     */
    public String getInquireBalanceJsonText(String cano, String acnt_prdt_cd, String afhr_flpr_yn, String ofl_yn, String inqr_dvsn, String unpr_dvsn, String fund_sttl_icld_yn, String fncg_amt_auto_rdpt_yn
            , String prcs_dvsn, String ctx_area_fk100, String ctx_area_nk100
    ){

        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/trading/inquire-balance";

        String trId ;
        if(koreainvestmentApi.isActual()){
            trId = "TTTC8434R";
        }else{
            trId = "VTTC8434R";
        }

        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id", trId);
        String query = "?cano="+ cano +"&acnt_prdt_cd=" + acnt_prdt_cd +"&afhr_flpr_yn=" + afhr_flpr_yn +"&ofl_yn=" +ofl_yn +"&inqr_dvsn=" + inqr_dvsn + "&unpr_dvsn=" + unpr_dvsn
                + "&fund_sttl_icld_yn=" + fund_sttl_icld_yn + "&fncg_amt_auto_rdpt_yn=" + fncg_amt_auto_rdpt_yn + "&prcs_dvsn=" + prcs_dvsn + "&ctx_area_fk100=" + ctx_area_fk100  + "&ctx_area_nk100=" + ctx_area_nk100;

        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new KoreainvestmentApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }
        return response.getMessage();
    }
    public BigDecimal getD2Cash(String accountNumberValue){
        String jsonText = getInquireBalanceJsonText(accountNumberValue);
        return  parsingD2Cash(jsonText);
    }


    public BigDecimal parsingD2Cash(String jsonText){
        JSONObject jsonObject = new JSONObject(jsonText);
        jsonObject = jsonObject.getJSONArray("output2").getJSONObject(0);
        return jsonObject.getBigDecimal("prvs_rcdl_excc_amt");
    }

    public String getInquireAccountBalanceJsonText(String accountNumberValue ){
        String cano = accountNumberValue.substring(0,8);
        String acnt_prdt_cd = accountNumberValue.substring(accountNumberValue.length()-2 );
        return getInquireAccountBalanceJsonText(cano, acnt_prdt_cd);
    }

    /**
     * Output1	응답상세	Object Array	Y		[아래 순서대로 출력 : 19항목]
     * 1: 주식
     * 2: 펀드/MMW
     * 3: 채권
     * 4: ELS/DLS
     * 5: WRAP
     * 6: 신탁/퇴직연금/외화신탁
     * 7: RP/발행어음
     * 8: 해외주식
     * 9: 해외채권
     * 10: 금현물
     * 11: CD/CP
     * 12: 단기사채
     * 13: 타사상품
     * 14: 외화단기사채
     * 15: 외화 ELS/DLS
     * 16: 외화
     * 17: 예수금+CMA
     * 18: 청약자예수금
     * 19: <합계>
     */
    public String getInquireAccountBalanceJsonText(String cano, String acnt_prdt_cd){
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/trading/inquire-account-balance";

        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(url,"tr_id", "CTRP6548R");
        String query = "?cano="+ cano +"&acnt_prdt_cd=" + acnt_prdt_cd +"&inqr_dvsn_1=&bspr_bf_dt_aply_yn=";

        HttpApiResponse response =  koreainvestmentApi.getHttpGet().getResponse(url + query, requestHeaderMap);
        if(response.getResponseCode() != 200){
            throw new KoreainvestmentApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }
        return response.getMessage();
    }



}
