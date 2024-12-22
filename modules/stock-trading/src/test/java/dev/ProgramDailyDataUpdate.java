package dev;

import io.runon.stock.trading.data.daily.ProgramDaily;
import io.runon.trading.data.TextChange;
import io.runon.trading.data.file.TimeFiles;
import org.json.JSONObject;

/**
 * @author macle
 */
public class ProgramDailyDataUpdate {



    public static void main(String[] args) {

//        String rowText = "{\"t\":1729728000000,\"ymd\":20241024,\"close\":56600,\"volume\":31499922,\"trading_price\":1809877767246,\"change\":-2500,\"net_buy_volume\":-3604892,\"net_buy_price\":-207079819300,\"net_buy_change_volume\":-5696367,\"net_buy_change_price\":-328608958300,\"sell_volume\":6527897,\"sell_price\":374997085400,\"buy_volume\":2923005,\"buy_price\":167917266100}";
//        ProgramDaily daily = ProgramDaily.make(rowText);
//        System.out.println(daily);
//        JSONObject row = new JSONObject(rowText);
//        daily.setAmount(row.getBigDecimal("trading_price"));
//        daily.setNetBuyAmount(row.getBigDecimal("net_buy_price"));
//        daily.setNetBuyChangeAmount(row.getBigDecimal("net_buy_change_price"));
//        daily.setSellAmount(row.getBigDecimal("sell_price"));
//        daily.setBuyAmount(row.getBigDecimal("buy_price"));
//        System.out.println(daily);
//
//        System.out.println(row.keySet().size());
//        System.out.println(new JSONObject(daily.outTimeLineJsonText()).keySet().size());


        TextChange textChange = text -> {
            try{

                //바뀐 변수명만 처리
                JSONObject row1 = new JSONObject(text);
                ProgramDaily daily1 = ProgramDaily.make(text);
                if(!row1.isNull("trading_price")) {
                    daily1.setAmount(row1.getBigDecimal("trading_price"));
                }
                if(!row1.isNull("net_buy_price")) {
                    daily1.setNetBuyAmount(row1.getBigDecimal("net_buy_price"));
                }
                if(!row1.isNull("net_buy_change_price")) {
                    daily1.setNetBuyChangeAmount(row1.getBigDecimal("net_buy_change_price"));
                }

                if(!row1.isNull("sell_price")) {
                    daily1.setSellAmount(row1.getBigDecimal("sell_price"));
                }
                if(!row1.isNull("buy_price")) {
                    daily1.setBuyAmount(row1.getBigDecimal("buy_price"));
                }



                return daily1.outTimeLineJsonText();

            }catch (Exception e){
                e.printStackTrace();
                return text;
            }

        };

        TimeFiles.changeTimeFileUpDirs("D:\\runon\\data\\stock\\KOR\\spot\\program",textChange);

    }
}
