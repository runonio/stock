package io.runon.stock.trading.exception;

import io.runon.trading.exception.TradingApiException;

/**
 * api를 활용하다 예상하지 않은 예외가 발생했을때
 * 예외가 명확히 정의되지 않은 케이스
 * @author macle
 */
public class StockApiException extends TradingApiException {

    /**
     * 생성자
     */
    public StockApiException(){
        super();
    }

    /**
     * 생성자
     * @param e 예외
     */
    public StockApiException(Exception e){
        super(e);
    }

    /**
     * 생성자
     * @param message exception message
     */
    public StockApiException(String message){
        super(message);
    }
}