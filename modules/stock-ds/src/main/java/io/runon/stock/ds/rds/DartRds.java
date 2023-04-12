package io.runon.stock.ds.rds;
/**
 * 저장된 dart 데이터 활용
 * @author macle
 */
public class DartRds {

    /**
     *  opendart.fss.or.kr/api/fnlttSinglAcntAll.json
     *  opendart.fss.or.kr/guide/detail.do?apiGrpCd=DS003&apiId=2019016
     *  제무제표 전체 데이터 원본
     */
    public static String fnlttSinglAcntAll(String symbol, int year, int quarter){
        return StockApiData.getData("KOR_" + symbol, "opendart.fss.or.kr/api/fnlttSinglAcntAll.json", year +"," + quarter);
    }
    
}
