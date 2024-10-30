package io.runon.stock.trading.data.management.db.sync;

import com.seomse.commons.callback.GenericCallBack;
import com.seomse.commons.config.Config;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.PrepareStatements;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.objects.JdbcObjects;
import com.seomse.jdbc.sync.JdbcSync;
import io.runon.stock.trading.*;
import io.runon.trading.data.*;
import io.runon.trading.data.jdbc.TradingJdbc;
import io.runon.trading.system.Category;
import io.runon.trading.system.CategoryCode;
import io.runon.trading.system.CategoryKeyValue;
import io.runon.trading.system.CommonConfig;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private final List<Class<?>> timeTableClassList = new ArrayList<>();

    private final String [] sequences = {
        "seq_issue_shares_history"
    };

    private final Object lock = new Object();

    //동기화 구간에서 동작해야 하기때문에 싱글턴 처리 단 다른 프로세스에서 발생한 문제는 해결할 수 없다.
    private StockDbSync(){
        timeTableClassList.add(Stock.class);
        timeTableClassList.add(StockGroup.class);
        timeTableClassList.add(StockGroupMap.class);
        timeTableClassList.add(Exchange.class);

        timeTableClassList.add(Futures.class);
        timeTableClassList.add(Bonds.class);
        timeTableClassList.add(Indices.class);
        timeTableClassList.add(Currencies.class);

        //이제 데이터 양이 큰거.
        timeTableClassList.add(IssueSharesHistory.class);
        timeTableClassList.add(DailyData.class);
        timeTableClassList.add(TimeText.class);

        timeTableClassList.add(StockApiData.class);
        timeTableClassList.add(StockDailyData.class);
        
        //이벤트
        timeTableClassList.add(EventCalendar.class);
        timeTableClassList.add(EventCalendarItem.class);

        //시스템 관련 테이블 동기화
        timeTableClassList.add(CommonConfig.class);
        timeTableClassList.add(Category.class);
        timeTableClassList.add(CategoryCode.class);
        timeTableClassList.add(CategoryKeyValue.class);

    }

    public void sync(){
        //시간값을 이용하여 업데이트만 하면 되는 간단한 테이블
        //일별정보 복사
        for(Class<?> tableClass : timeTableClassList){
            timeTablesSync(tableClass);
        }
        
        //시퀀스 싱크
        try(Connection selectConn = JdbcSync.newSyncServerConnection()){
            for(String sequence : sequences){
                Long value = JdbcQuery.getResultLong(selectConn, "select last_value from " + sequence);
                if(value== null){
                    continue;
                }
                JdbcQuery.getResultLong("select setval('" + sequence +"', " +value +")");
            }

        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }
    }

    public void timeTablesSync(Class<?> tableClass){
        synchronized (lock){
            Table table = tableClass.getAnnotation(Table.class);
            log.debug("sync table: " + table.name());

            try(Connection selectConn = JdbcSync.newSyncServerConnection()){
                GenericCallBack<Object> callBack  = o -> {

                    try {
                        String where = JdbcObjects.getCheckWhere(o);

                        Object source = JdbcObjects.getObj(tableClass, where);

                        if(source == null){
                            JdbcObjects.insert(o);
                        }else{
                            if(!TradingJdbc.equalsOutUpdatedAt(source, o)){
                                JdbcObjects.update(o);
                            }
                        }
                    }catch (Exception e){
                        log.error(ExceptionUtil.getStackTrace(e));
                    }

                };

                Long maxTime = JdbcQuery.getResultDateTime("select max(updated_at) from " + table.name());
                if(maxTime == null){
                    JdbcObjects.callbackObj(selectConn, tableClass, null, null, "updated_at asc", -1, null, callBack);
                }else{
                    Map<Integer, PrepareStatementData> prepareStatementDataMap =  PrepareStatements.newTimeMap(maxTime);
                    JdbcObjects.callbackObj(selectConn, tableClass, null, "updated_at >= ?", "updated_at asc", -1, prepareStatementDataMap, callBack);
                }

            }catch (Exception e){
                log.error(ExceptionUtil.getStackTrace(e));
            }
        }


    }

    public static void main(String[] args) throws IllegalAccessException {
        Config.getConfig("");
//
//        Stock stock = Stocks.getStock("KOR_500069");
//
//        String where = JdbcObjects.getCheckWhere(stock);
//        System.out.println(where);

        StockDbSync stockDbSync = StockDbSync.getInstance();
        stockDbSync.sync();
    }



}
