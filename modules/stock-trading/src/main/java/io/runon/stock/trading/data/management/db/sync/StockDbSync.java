package io.runon.stock.trading.data.management.db.sync;

import com.seomse.commons.config.Config;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.connection.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

/**
 * 주식관련 DB 데이터 동기화
 * 데이터 백업용
 * 동기화 구간에서 동작해야 하기때문에 싱글턴 처리
 * 단 다른 프로세스에서 발생한 문제는 해결할 수 없다.
 * @author macle
 */
@Slf4j
public class StockDbSync {


    private static class Singleton {
        private static final StockDbSync instance = new StockDbSync();
    }

    public static StockDbSync getInstance(){
        return Singleton.instance;
    }

    private final String [] timeSyncTables ={
            "stock"
            , "stock_group"
            , "stock_group_map"
            , "exchange"
            , "futures"
            , "indices"
            , "bonds"
            , "currencies"
    };

    private final String [] syncSequence = {


    };


    private final Object lock = new Object();

    //동기화 구간에서 동작해야 하기때문에 싱글턴 처리 단 다른 프로세스에서 발생한 문제는 해결할 수 없다.
    private StockDbSync(){

    }

    public void sync(){
        //시간값을 이용하여 업데이트만 하면 되는 간단한 테이블
        //일별정보 복사
    }

    public void timeTablesSync(){
        synchronized (lock){

            try(Connection selectConn = StockJdbc.newSyncServerConnection()){



            }catch (Exception e){
                log.error(ExceptionUtil.getStackTrace(e));
            }


        }


    }




}
