package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.google.gson.JsonObject;
import com.seomse.commons.data.StringArray;
import com.seomse.commons.http.HttpApis;
import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.utils.GsonUtils;
import com.seomse.jdbc.objects.JdbcObjects;
import io.runon.stock.trading.StockGroup;
import io.runon.stock.trading.StockGroupMap;
import io.runon.stock.trading.data.StockReg;
import io.runon.trading.CountryCode;
import io.runon.trading.data.Futures;
import io.runon.trading.data.TradingDataPath;
import io.runon.trading.data.jdbc.TradingJdbc;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

/**
 * @author macle
 */
public class KoreainvestmentFiles {

    public static void updateDownloadThemeList() {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        File file = HttpApis.downloadFile("https://new.real.download.dws.co.kr/common/master/theme_code.mst.zip", TradingDataPath.getTempPath("koreainvestment" + fileSeparator + "theme_code.mst.zip"));
        File [] unZipFiles =FileUtil.unZip(file);
        for(File unZipFile : unZipFiles){
            String fileText = FileUtil.getFileContents(unZipFile,"EUC-KR");
            updateThemeGroup(fileText);
            //noinspection ResultOfMethodCallIgnored
            unZipFile.delete();
        }

        //noinspection ResultOfMethodCallIgnored
        file.delete();

    }

    public static void updateThemeGroup(String text){

        Map<String, StockGroup> map = new HashMap<>();

        StockGroupMap stockGroupMap = new StockGroupMap();
        StringArray [] themeArray = getThemeArray(text);
        for(StringArray theme : themeArray){
            String groupId = "koreainvestment_theme_" + theme.get(0);
            String name = theme.get(1);
            String symbol = theme.get(2);

            if(!map.containsKey(groupId)){
                StockGroup stockGroup = new StockGroup();
                stockGroup.setStockGroupId(groupId);
                stockGroup.setGroupType("THEME");
                stockGroup.setCountry(CountryCode.KOR.toString());
                stockGroup.setNameKo(name);
                TradingJdbc.updateTimeCheck(stockGroup);
                map.put(groupId, stockGroup);
            }

            stockGroupMap.setStockGroupId(groupId);
            stockGroupMap.setStockId("KOR_" + symbol);
            stockGroupMap.setUpdatedAt(System.currentTimeMillis());
            TradingJdbc.updateTimeCheck(stockGroupMap);
        }
//        koreainvestment_theme_
    }


    public static StringArray[] getThemeArray(String text){

        String [] lines = text.split("\n");

        StringArray [] themeArray = new StringArray[lines.length];
        for (int i = 0; i <themeArray.length ; i++) {
            String line = lines[i];
            String code = line.substring(0,3);
            String name = line.substring(3, line.length()-10).trim();
            String symbol = line.substring(line.length()-10).trim();

            StringArray theme  = new StringArray(code, name, symbol);
            themeArray[i] = theme;

        }

        return themeArray;
    }


    public static void updateDownloadFuturesList(){
        String fileSeparator = FileSystems.getDefault().getSeparator();

        File file = HttpApis.downloadFile("https://new.real.download.dws.co.kr/common/master/fo_stk_code_mts.mst.zip", TradingDataPath.getTempPath("koreainvestment" + fileSeparator + "fo_stk_code_mts.mst.zip"));
        File [] unZipFiles = FileUtil.unZip(file);
        for(File unZipFile : unZipFiles){

            String fileText = FileUtil.getFileContents(unZipFile,"EUC-KR");
            Futures [] futuresArray = getFutures(fileText);
            for(Futures futures : futuresArray){
                Futures saveObj = JdbcObjects.getObj(Futures.class, "standard_code='" +  futures.getStandardCode() + "'");
                if(saveObj == null){
                    continue;
                }
                futures.setFuturesId(saveObj.getFuturesId());
                TradingJdbc.updateTimeCheck(futures);
            }

            //noinspection ResultOfMethodCallIgnored
            unZipFile.delete();
        }

        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    public static Futures[] getFutures(String mstFileContent){

        String [] lines = mstFileContent.split("\n");
        Futures [] array = new Futures[lines.length];
        for (int i = 0; i <lines.length ; i++) {
            String line = lines[i];
            String [] values = line.split("\\|");

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("koreainvestment", values[1]);

            Futures futures = new Futures();
            futures.setProductType(null);
            futures.setStandardCode(values[2]);
            futures.setNameKo(values[3]);
            futures.setUnderlyingAssetsId(values[7]);
            futures.setDataValue(GsonUtils.toJson(jsonObject));
            array[i] = futures;
        }

        return array;
    }


    public final static StringArray [] USA_EXCHANGES = {
            new StringArray("nys","NYSE","뉴욕")
        , new StringArray("nas","NASDAQ","나스닥")
        , new StringArray("ams","AMEX","아멕스")
    };


    public static void updateOverseasStocks() {


        //중요도가 낮은 시장부터, 혹시나 symbol이 겹칠경우 중요도가 높은시장 종목만 사용한다.
        for (int i = USA_EXCHANGES.length-1; i > -1 ; i--) {
            updateOverseasStocks(USA_EXCHANGES[i]);
        }

    }
    public static void updateOverseasStocks(StringArray exchange) {
        String fileSeparator = FileSystems.getDefault().getSeparator();

        String url = "https://new.real.download.dws.co.kr/common/master/{val}mst.cod.zip".replace("{val}",exchange.get(0));
        String downloadPath = TradingDataPath.getTempPath("koreainvestment" + fileSeparator + exchange.get(0) + "mst.cod.zip");
        File file = HttpApis.downloadFile(url, downloadPath);

        File [] unZipFiles =FileUtil.unZip(file);
        for(File unZipFile : unZipFiles){
            String fileText = FileUtil.getFileContents(unZipFile,"EUC-KR");
            updateOverseasStocks(exchange, fileText);
            //noinspection ResultOfMethodCallIgnored
            unZipFile.delete();

        }
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    public static void updateOverseasStocks(StringArray exchange, String unzipFileText) {
//        columns = ['National code', 'Exchange id', 'Exchange code', 'Exchange name', 'Symbol', 'realtime symbol', 'Korea name', 'English name', 'Security type(1:Index,2:Stock,3:ETP(ETF),4:Warrant)', 'currency', 'float position', 'data type', 'base price', 'Bid order size', 'Ask order size', 'market start time(HHMM)', 'market end time(HHMM)', 'DR 여부(Y/N)', 'DR 국가코드', '업종분류코드', '지수구성종목 존재 여부(0:구성종목없음,1:구성종목있음)', 'Tick size Type', '구분코드(001:ETF,002:ETN,003:ETC,004:Others,005:VIX Underlying ETF,006:VIX Underlying ETN)','Tick size type 상세']


        String [] lines = unzipFileText.split("\n");
        for(String line: lines){
            String [] values =line.split("\t");

            if(values[8].equals("2") || values[8].equals("3")){
                StockReg stockReg = new StockReg();
                stockReg.setSymbol(values[4]);
                stockReg.setStockId("USA_" + stockReg.getSymbol());
                stockReg.setExchange(exchange.get(1));
                stockReg.setNameKo(values[6]);
                stockReg.setNameEn(values[7]);
                if(values[8].equals("2")) {
                    stockReg.setStockType("STOCK");
                }else{
                    stockReg.setStockType("ETF");
                }

                TradingJdbc.updateTimeCheck(stockReg);

            }
        }
    }



    public static void main(String[] args) {
//        updateDownloadFuturesList();

//        getThemeArray(FileUtil.getFileContents("theme_code.mst","EUC-KR"));
        updateOverseasStocks();
    }

}
