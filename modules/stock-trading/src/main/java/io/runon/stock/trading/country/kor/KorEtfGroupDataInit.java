package io.runon.stock.trading.country.kor;

import io.runon.commons.config.Config;
import io.runon.commons.utils.ExceptionUtil;
import io.runon.jdbc.objects.JdbcObjects;
import io.runon.jdbc.sync.JdbcSync;
import io.runon.stock.trading.data.management.db.sync.StockDbSync;
import io.runon.stock.trading.group.StockGroup;
import io.runon.stock.trading.group.StockGroupMap;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

/**
 * ETF 타겟종목 초기 매핑
 * @author macle
 */
@Slf4j
public class KorEtfGroupDataInit {

    KorEtfGroupDataInit(){

    }

    public void init(){
        //서버에 업데이트 이후에 db sync
        try(Connection selectConn = JdbcSync.newSyncServerConnection()){
            //etf
            //지수
            setIndexLongEtf(selectConn);
            setIndexShortEtf(selectConn);
            //채권
            setBondLongEtf(selectConn);
            setBondShortEtf(selectConn);
            //원자재
            setCommoditiesLongEtf(selectConn);
            setCommoditiesShortEtf(selectConn);
            //외환
            setCurrenciesLongEtf(selectConn);
            setCurrenciesShortEtf(selectConn);

            Thread.sleep(5000L);

            StockDbSync.getInstance().sync();
            KorStocks.updateStockType();
        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }
    }

    public void setIndexLongEtf(Connection connection){
        //한국시장에 상장한 코스피, 코스닥, S&P500, 나스닥 long 포지션 관련 etf
        StockGroup stockGroup = new StockGroup();
        stockGroup.setStockGroupId("kor_index_long_etf");
        stockGroup.setCountry("KOR");
        stockGroup.setGroupType("ETF");
        stockGroup.setNameKo("지수 long etf");
        stockGroup.setNameEn("index long etf");
        stockGroup.setDescription("한국 시장에 상장한 지수 long 포지션 etf, 미국지수를 기반으로한 etf 포함");
        stockGroup.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(connection, stockGroup);

        StockGroupMap groupMap = new StockGroupMap();
        groupMap.setStockGroupId(stockGroup.getStockGroupId());

        //kodex 200
        groupMap.setStockId("KOR_069500");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        //kodex 코스닥 150
        groupMap.setStockId("KOR_229200");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        //나스닥 100
        groupMap.setStockId("KOR_133690");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        //S&P 500
        groupMap.setStockId("KOR_360750");
        JdbcObjects.insertOrUpdate(connection, groupMap);

    }


    public void setIndexShortEtf(Connection connection){
        StockGroup stockGroup = new StockGroup();
        stockGroup.setStockGroupId("kor_index_short_etf");
        stockGroup.setCountry("KOR");
        stockGroup.setGroupType("ETF");
        stockGroup.setNameKo("지수 short etf");
        stockGroup.setNameEn("index short etf");
        stockGroup.setDescription("한국 시장에 상장한 지수 short 포지션 etf, 미국지수를 기반으로한 etf 포함");
        stockGroup.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(connection, stockGroup);

        StockGroupMap groupMap = new StockGroupMap();
        groupMap.setStockGroupId(stockGroup.getStockGroupId());

        //kodex 200
        groupMap.setStockId("KOR_114800");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        //kodex 코스닥 150
        groupMap.setStockId("KOR_251340");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        //나스닥 100
        groupMap.setStockId("KOR_409810");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        //S&P 500
        groupMap.setStockId("KOR_225030");
        JdbcObjects.insertOrUpdate(connection, groupMap);
    }

    public void setBondLongEtf(Connection connection){
        StockGroup stockGroup = new StockGroup();
        stockGroup.setStockGroupId("kor_bond_long_etf");
        stockGroup.setCountry("KOR");
        stockGroup.setGroupType("ETF");
        stockGroup.setNameKo("채권 long etf");
        stockGroup.setNameEn("bond long etf");
        stockGroup.setDescription("한국 시장에 상장한 채권 long 포지션 etf, 미국 채권 포함, 금리 인하 배팅");
        stockGroup.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(connection, stockGroup);

        StockGroupMap groupMap = new StockGroupMap();
        groupMap.setStockGroupId(stockGroup.getStockGroupId());

        //국고채 10년
        groupMap.setStockId("KOR_148070");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        //국고채 30년
        groupMap.setStockId("KOR_439870");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        //미국채 30년 (환햇지 X)
        groupMap.setStockId("KOR_476760");
        JdbcObjects.insertOrUpdate(connection, groupMap);

    }

    public void setBondShortEtf(Connection connection){
        StockGroup stockGroup = new StockGroup();
        stockGroup.setStockGroupId("kor_bond_short_etf");
        stockGroup.setCountry("KOR");
        stockGroup.setGroupType("ETF");
        stockGroup.setNameKo("채권 short etf");
        stockGroup.setNameEn("bond short etf");
        stockGroup.setDescription("한국 시장에 상장한 채권 short 포지션 etf, 미국 채권 포함, 금리 인상 배팅");
        stockGroup.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(connection, stockGroup);

        StockGroupMap groupMap = new StockGroupMap();
        groupMap.setStockGroupId(stockGroup.getStockGroupId());

        //국고채 10년 인버스
        groupMap.setStockId("KOR_295020");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        //미국채 30년 인버스 (환햇지 O)
        groupMap.setStockId("KOR_304670");
        JdbcObjects.insertOrUpdate(connection, groupMap);
    }



    public void setCommoditiesLongEtf(Connection connection){
        //금,은, 오일,

        StockGroup stockGroup = new StockGroup();
        stockGroup.setStockGroupId("kor_commodities_long_etf");
        stockGroup.setCountry("KOR");
        stockGroup.setGroupType("ETF");
        stockGroup.setNameKo("원자재 long etf");
        stockGroup.setNameEn("bond long etf");
        stockGroup.setDescription("commodities 시장에 상장한 원자재 long 포지션 etf");
        stockGroup.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(connection, stockGroup);

        StockGroupMap groupMap = new StockGroupMap();
        groupMap.setStockGroupId(stockGroup.getStockGroupId());

        //krx 금현물
        groupMap.setStockId("KOR_411060");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        // KODEX 은선물(H)
        groupMap.setStockId("KOR_144600");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        // KODEX WTI원유선물(H)
        groupMap.setStockId("KOR_261220");
        JdbcObjects.insertOrUpdate(connection, groupMap);
    }


    public void setCommoditiesShortEtf(Connection connection){
        StockGroup stockGroup = new StockGroup();
        stockGroup.setStockGroupId("kor_commodities_short_etf");
        stockGroup.setCountry("KOR");
        stockGroup.setGroupType("ETF");
        stockGroup.setNameKo("원자재 short etf");
        stockGroup.setNameEn("bond short etf");
        stockGroup.setDescription("commodities 시장에 상장한 원자재 short 포지션 etf");
        stockGroup.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(connection, stockGroup);

        StockGroupMap groupMap = new StockGroupMap();
        groupMap.setStockGroupId(stockGroup.getStockGroupId());

        // KODEX 골드선물인버스(H), 22
        groupMap.setStockId("KOR_280940");
        JdbcObjects.insertOrUpdate(connection, groupMap);

        //KODEX WTI원유선물인버스(H)
        groupMap.setStockId("KOR_271050");
        JdbcObjects.insertOrUpdate(connection, groupMap);
    }

        
    public void setCurrenciesLongEtf(Connection connection){
        //초기 목표는 달러만
        StockGroup stockGroup = new StockGroup();
        stockGroup.setStockGroupId("kor_currencies_long_etf");
        stockGroup.setCountry("KOR");
        stockGroup.setGroupType("ETF");
        stockGroup.setNameKo("환율 long etf");
        stockGroup.setNameEn("currencies long etf");
        stockGroup.setDescription("외환 환율 long 포지션 etf 환율 상승, 원화가치가 다른 통화에 비해 상대적 하락에 베팅");
        stockGroup.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(connection, stockGroup);

        StockGroupMap groupMap = new StockGroupMap();
        groupMap.setStockGroupId(stockGroup.getStockGroupId());

        // TIGER 미국달러단기채권액티브
        groupMap.setStockId("KOR_329750");
        JdbcObjects.insertOrUpdate(connection, groupMap);


    }

    public void setCurrenciesShortEtf(Connection connection){
        //달러
        StockGroup stockGroup = new StockGroup();
        stockGroup.setStockGroupId("kor_currencies_short_etf");
        stockGroup.setCountry("KOR");
        stockGroup.setGroupType("ETF");
        stockGroup.setNameKo("환율 short etf");
        stockGroup.setNameEn("currencies short etf");
        stockGroup.setDescription("외환 환율 short 포지션 etf 환율 상승, 원화가치가 다른 통화에 비해 상대적 상승에 베팅");
        stockGroup.setUpdatedAt(System.currentTimeMillis());

        JdbcObjects.insertOrUpdate(connection, stockGroup);

        StockGroupMap groupMap = new StockGroupMap();
        groupMap.setStockGroupId(stockGroup.getStockGroupId());

        // TKODEX 미국달러선물인버스
        groupMap.setStockId("KOR_261270");
        JdbcObjects.insertOrUpdate(connection, groupMap);
    }



    public static void main(String[] args) throws Exception{
        Config.getConfig("");
        new KorEtfGroupDataInit().init();
    }



}
