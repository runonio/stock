package io.runon.stock.trading.data;
/**
 * 데이터 연결유형
 * @author macle
 */
public enum DataConnectType {
    DB // DB직접 연동 고객 서비스 개발전에는 직접 연동함
    , API // Rest Api 활용 고객 서비스가 나올떄쯤에는 api를 기본으로 사용한다. 고객용을 처음부터 너무 신경썻더니 정작 중요한 개발에 속도가 늦어짐.

}
