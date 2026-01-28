package io.runon.stock.securities.firm.api.kor.koreainvestment.account;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.runon.commons.utils.GsonUtils;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.stock.trading.Stock;
import io.runon.stock.trading.StockHolding;
import io.runon.stock.trading.Stocks;
import io.runon.stock.trading.account.StockAccount;
import io.runon.stock.trading.exception.StockDataException;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 한국투자증권 계좌관련 API정리
 *  API가 많아서 정리한 클래스를 나눈다.
 * @author macle
 */
@Setter
public class KoreainvestmentAccount implements StockAccount {

    private String id = KoreainvestmentApi.getAccountNumber();

    private StockHolding [] stockHoldings;


    private Map<Integer, BigDecimal> amountMap = new HashMap<>();


    public KoreainvestmentAccount() {

        setData();
    }

    public void setData(){
        KoreainvestmentAccountApi accountApi = KoreainvestmentApi.getInstance().getAccountApi();
        String text = accountApi.getInquireBalanceJsonText();

        JsonObject jsonObject = GsonUtils.fromJsonObject(text);

        JsonArray holdingArray = jsonObject.get("output1").getAsJsonArray();

        StockHolding [] stockHoldings = new  StockHolding[holdingArray.size()];

        for (int i = 0; i < stockHoldings.length ; i++) {
            JsonObject holding = holdingArray.get(i).getAsJsonObject();

            StockHolding stockHolding = new StockHolding();

            Stock stock = Stocks.getStock("KOR_" + holding.get("pdno").getAsString());

            if(stock == null){
                throw new StockDataException("stock null: " + "KOR_" + holding.get("pdno"));
            }

            stockHolding.setStock(stock);
            stockHolding.setAvgPrice(holding.get("pchs_avg_pric").getAsBigDecimal());
            stockHolding.setQuantity(holding.get("hldg_qty").getAsBigDecimal());
            stockHoldings[i] =  stockHolding;
        }

        this.stockHoldings = stockHoldings;


        JsonObject out2 = jsonObject.get("output2").getAsJsonArray().get(0).getAsJsonObject();

        amountMap.put(0, out2.get("dnca_tot_amt").getAsBigDecimal());
        amountMap.put(1, out2.get("nxdy_excc_amt").getAsBigDecimal());
        amountMap.put(2, out2.get("prvs_rcdl_excc_amt").getAsBigDecimal());
    }


    @Override
    public StockHolding[] getStockHoldings() {
        return stockHoldings;
    }

    @Override
    public BigDecimal getPrice(String stockId) {
        return KoreainvestmentApi.getInstance().getStockInfoApi().getPrice(Stocks.getSymbol(stockId));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BigDecimal getAssets() {
        //세금과 수수료의 합 세금 0.2%, 수수료 0.15%
        //% 이기때문에 총금에 곱하려면 100을나눈값
        BigDecimal feeRate = new BigDecimal("0.0035");


        BigDecimal assets = amountMap.get(2);

        for(StockHolding stockHolding : stockHoldings){
            BigDecimal price = getPrice(stockHolding.getStock().getStockId());
            BigDecimal total = price.multiply(stockHolding.getQuantity());

            BigDecimal fee = total.multiply(feeRate);

            BigDecimal last = total.subtract(fee);
            assets = assets.add(last);
        }

        return assets;
    }


    @Override
    public BigDecimal getCash(int nextDay) {
        if(nextDay > 2){
            nextDay = 2;
        }

        if(nextDay < 0){
            nextDay = 0;
        }


        return amountMap.get(nextDay);
    }

    public static void main(String[] args) {
        KoreainvestmentAccount account=  new KoreainvestmentAccount();

        System.out.println(account.getAssets().toPlainString());
    }

}
