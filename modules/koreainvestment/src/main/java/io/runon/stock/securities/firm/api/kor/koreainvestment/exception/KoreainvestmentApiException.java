package io.runon.stock.securities.firm.api.kor.koreainvestment.exception;

/**
 * 한국증권 api 요청 중 예외.
 * 예외가 명확히 정의되지 않은 케이스
 * @author macle
 */
public class KoreainvestmentApiException extends RuntimeException{

    /**
     * 생성자
     */
    public KoreainvestmentApiException(){
        super();
    }

    /**
     * 생성자
     * @param e 예외
     */
    public KoreainvestmentApiException(Exception e){
        super(e);
    }

    /**
     * 생성자
     * @param message exception message
     */
    public KoreainvestmentApiException(String message){
        super(message);
    }
}
