package io.runon.stock.trading.exception;
/**
 *
 * @author macle
 */
public class StockNotSupportedException extends RuntimeException{

    /**
     * 생성자
     */
    public StockNotSupportedException(){
        super();
    }

    /**
     * 생성자
     * @param e 예외
     */
    public StockNotSupportedException(Exception e){
        super(e);
    }

    /**
     * 생성자
     * @param message exception message
     */
    public StockNotSupportedException(String message){
        super(message);
    }
}
