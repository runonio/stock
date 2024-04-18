package example.price;

import com.seomse.commons.utils.FileUtil;
import io.runon.stock.securities.firm.api.kor.koreainvestment.KoreainvestmentApi;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 기간별 가격 데이터와 정보
 * 한국투자증권 오리지날 데이터
 * @author macle
 */
public class PricePeriodJsonFileRead {
    public static void main(String[] args) {
        String text = FileUtil.getFileContents(new File("modules/koreainvestment/json/price_period.json"), StandardCharsets.UTF_8);
//        System.out.println(text);


        KoreainvestmentApi api = KoreainvestmentApi.getInstance();
        TradeCandle [] candles = api.getPeriodDataApi().getCandles(text);

        for(TradeCandle candle : candles){
            System.out.println(candle);
        }
    }



}
