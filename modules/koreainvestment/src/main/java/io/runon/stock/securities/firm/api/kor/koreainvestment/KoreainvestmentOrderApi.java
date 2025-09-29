package io.runon.stock.securities.firm.api.kor.koreainvestment;

import io.runon.commons.exception.UndefinedException;
import io.runon.commons.apis.http.HttpApiResponse;
import io.runon.stock.trading.country.kor.KorStocks;
import io.runon.stock.trading.exception.StockApiException;
import io.runon.trading.Trade;
import io.runon.trading.order.*;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author macle
 */
public class KoreainvestmentOrderApi implements LimitOrder, MarketOrder {

    private final KoreainvestmentApi koreainvestmentApi;
    public KoreainvestmentOrderApi(KoreainvestmentApi koreainvestmentApi){
        this.koreainvestmentApi = koreainvestmentApi;
    }

    /**
     * order code
     * 00@지정가
     * 01@시장가
     * 02@조건부지정가
     * 03@최유리지정가
     * 04@최우선지정가
     * 05@장전 시간외
     * 06@장후 시간외
     * 07@시간외 단일가
     * 65@경매매
     * 08@자기주식
     * 09@자기주식S-Option
     * 10@자기주식금전신탁
     * 11@IOC지정가 (즉시체결,잔량취소)
     * 12@FOK지정가 (즉시체결,전량취소)
     * 13@IOC시장가 (즉시체결,잔량취소)
     * 14@FOK시장가 (즉시체결,전량취소)
     * 15@IOC최유리 (즉시체결,잔량취소)
     * 16@FOK최유리 (즉시체결,전량취소)
     * 51@장중대량
     * 52@장중바스켓
     * 62@장개시전 시간외대량
     * 63@장개시전 시간외바스켓
     * 67@장개시전 금전신탁자사주
     * 69@장개시전 자기주식
     * 72@시간외대량
     * 77@시간외자사주신탁
     * 79@시간외대량자기주식
     * 80@바스켓
     * 21@중간가
     * 22@스톱지정가
     * 23@중간가IOC
     * 24@중간가FOK
     */
    public String orderJsonText(String cano, String prdt, String symbol, String exchange ,Trade.Type tradeType ,String orderCode, BigDecimal quantity , BigDecimal price ){

        //https://apiportal.koreainvestment.com/apiservice-apiservice?/uapi/domestic-stock/v1/trading/order-cash
        koreainvestmentApi.updateAccessToken();
        String url = "/uapi/domestic-stock/v1/trading/order-cash" ;

        String headerKey = url + "@" + tradeType;;

        String trId;
        if(tradeType == Trade.Type.BUY){
            trId = "TTTC0012U";
        }else if(tradeType == Trade.Type.SELL){
            trId = "TTTC0011U";
        }else {
            throw new UndefinedException("trade type not supported: " + tradeType);
        }

        if(exchange == null || exchange.isEmpty()){
            exchange = "KRX";
        }

        Map<String, String> requestHeaderMap = koreainvestmentApi.computeIfAbsenttPropertySingleMap(headerKey,"tr_id", trId);

        String priceValue;

        if(price == null){
            priceValue = "0";
        }else{
            priceValue = price.stripTrailingZeros().toPlainString();
        }

        JSONObject paramObject = new JSONObject();
        paramObject.put("CANO", cano);
        paramObject.put("ACNT_PRDT_CD", prdt);
        paramObject.put("PDNO", symbol);
        paramObject.put("ORD_DVSN", orderCode);

        paramObject.put("ORD_QTY", quantity.toPlainString());
        paramObject.put("ORD_UNPR", priceValue);
        paramObject.put("EXCG_ID_DVSN_CD", exchange);

        HttpApiResponse response =  koreainvestmentApi.getHttpPost().getResponse(url , requestHeaderMap, paramObject.toString());

        if(response.getResponseCode() != 200){
            throw new StockApiException("token make fail code:" + response.getResponseCode() +", " + response.getMessage());
        }

        return response.getMessage();
    }

    @Override
    public LimitOrderTrade limitOrderQuantity(String idOrSymbol, String exchange, Trade.Type type, BigDecimal quantity, BigDecimal limitPrice) {

        String symbol = KorStocks.getSymbol(idOrSymbol);
        String jsonText = orderJsonText(KoreainvestmentApi.getCano(), KoreainvestmentApi.getAccountProductCode(), idOrSymbol, exchange, type,   "00", quantity, limitPrice);


        return null;
    }

    @Override
    public LimitOrderCashTrade limitOrderCash(String idOrSymbol, String exchange, Trade.Type type, BigDecimal cash, BigDecimal limitPrice) {
        return null;
    }

    @Override
    public LimitOrderCashTrade limitOrderCash(String idOrSymbol, String exchange, Trade.Type type, BigDecimal cash, BigDecimal beginPrice, BigDecimal endPrice, BigDecimal priceGap) {
        return null;
    }

    @Override
    public MarketOrderTrade marketOrderQuantity(String idOrSymbol, String exchange, Trade.Type type, BigDecimal quantity) {
        return null;
    }

    @Override
    public MarketOrderTrade marketOrderCash(String idOrSymbol, String exchange, Trade.Type type, BigDecimal cash) {


        return null;
    }



    @Override
    public MarketOrderTrade closePosition(String idOrSymbol, String exchange) {
        return null;
    }


    public static void main(String[] args) {

    }
}
