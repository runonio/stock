package io.runon.stock.trading.exception;
/**
 * @author macle
 */
public class StockDataException extends RuntimeException{

    /**
     * 생성자
     */
    public StockDataException(){
        super();
    }

    /**
     * 생성자
     * @param e 예외
     */
    public StockDataException(Exception e){
        super(e);
    }

    /**
     * 생성자
     * @param message exception message
     */
    public StockDataException(String message){
        super(message);
    }
}
