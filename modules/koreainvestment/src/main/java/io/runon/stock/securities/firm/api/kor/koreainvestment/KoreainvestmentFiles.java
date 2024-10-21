package io.runon.stock.securities.firm.api.kor.koreainvestment;

import com.seomse.commons.http.HttpApis;
import com.seomse.commons.utils.FileUtil;
import io.runon.stock.trading.StockGroup;
import io.runon.stock.trading.StockGroupMap;
import io.runon.trading.CountryCode;
import io.runon.trading.data.Futures;
import io.runon.trading.data.StringArray;
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


    public static StringArray [] getThemeArray(String text){

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
        File [] unZipFiles =FileUtil.unZip(file);
        for(File unZipFile : unZipFiles){

            String fileText = FileUtil.getFileContents(unZipFile,"EUC-KR");
            Futures[] futuresArray = getFutures(fileText);
            TradingJdbc.updateTimeCheck(futuresArray);

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

            Futures futures = new Futures();
            futures.setSymbol(values[1]);
            futures.setUnderlyingAssetsType("STOCK");
            futures.setFuturesId("KOR_" + futures.getSymbol());
            futures.setStandardCode(values[2]);
            futures.setNameKo(values[3]);
            futures.setUnderlyingAssetsId(values[7]);
            array[i] = futures;
        }

        return array;
    }


    public static void main(String[] args) {
//        updateDownloadFuturesList();

//        getThemeArray(FileUtil.getFileContents("theme_code.mst","EUC-KR"));
        updateDownloadThemeList();
    }

}