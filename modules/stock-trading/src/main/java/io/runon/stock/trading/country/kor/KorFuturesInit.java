package io.runon.stock.trading.country.kor;

import io.runon.trading.data.jdbc.TradingJdbc;
import io.runon.trading.system.Category;
import io.runon.trading.system.CategoryKeyValue;
import lombok.extern.slf4j.Slf4j;

/**
 * @author macle
 */
@Slf4j
public class KorFuturesInit {

    public static void initYearSymbolMap(){

        Category category = new Category();
        category.setCategoryId("kor_futures_year_symbol");
        category.setNameKo("선물 기호별 연도");
        category.setDescription("한국 선물코드는 4번째자리에 년도를 알파벳기호로 표시함. 관련 매핑정보");
        TradingJdbc.updateTimeCheck(category);
        
        CategoryKeyValue categoryKeyValue = new CategoryKeyValue();
        categoryKeyValue.setCategoryId("kor_futures_year_symbol");

        updateKeyValue(categoryKeyValue, "A", "2006");
        updateKeyValue(categoryKeyValue, "B", "2007");
        updateKeyValue(categoryKeyValue, "C", "2008");
        updateKeyValue(categoryKeyValue, "D", "2009");
        updateKeyValue(categoryKeyValue, "E", "2010");
        updateKeyValue(categoryKeyValue, "F", "2011");
        updateKeyValue(categoryKeyValue, "G", "2012");
        updateKeyValue(categoryKeyValue, "H", "2013");
        updateKeyValue(categoryKeyValue, "J", "2014");
        updateKeyValue(categoryKeyValue, "K", "2015");
        updateKeyValue(categoryKeyValue, "L", "2016");
        updateKeyValue(categoryKeyValue, "M", "2017");
        updateKeyValue(categoryKeyValue, "N", "2018");

        updateKeyValue(categoryKeyValue, "P", "2019");
        updateKeyValue(categoryKeyValue, "Q", "2020");
        updateKeyValue(categoryKeyValue, "R", "2021");
        updateKeyValue(categoryKeyValue, "S", "2022");
        updateKeyValue(categoryKeyValue, "T", "2023");
        updateKeyValue(categoryKeyValue, "V", "2024");
        updateKeyValue(categoryKeyValue, "W", "2025");
    }

    public static void updateKeyValue(CategoryKeyValue categoryKeyValue, String key, String value){
        categoryKeyValue.setKey(key);
        categoryKeyValue.setValue(value);
        TradingJdbc.updateTimeCheck(categoryKeyValue);
    }

}
