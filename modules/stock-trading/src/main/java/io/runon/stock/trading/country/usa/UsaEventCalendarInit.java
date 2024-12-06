package io.runon.stock.trading.country.usa;

import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.calendar.EventCalendar;
import io.runon.trading.data.jdbc.TradingJdbc;

/**
 * ETF 타겟종목 초기 매핑
 * @author macle
 */
public class UsaEventCalendarInit {

    public static void futuresExpiration(){
        int [] ymds ={
                19900316, 19900615, 19900921, 19901221
                , 19910315, 19910621, 19910920, 19911220
                , 19920320, 19920619, 19920918, 19921218
                , 19930319, 19930618, 19930917, 19931217
                , 19940318, 19940617, 19940916, 19941216
                , 19950317, 19950616, 19950915, 19951215
                , 19960315, 19960621, 19960920, 19961220
                , 19970321, 19970620, 19970919, 19971219
                , 19980320, 19980619, 19980918, 19981218
                , 19990319, 19990618, 19990917, 19991217
                , 20000317, 20000616, 20000915, 20001215
                , 20010316, 20010615, 20010921, 20011221
                , 20020315, 20020621, 20020920, 20021220
                , 20030321, 20030620, 20030919, 20031219
                , 20040319, 20040618, 20040917, 20041217
                , 20050318, 20050617, 20050916, 20051216
                , 20060317, 20060616, 20060915, 20061215
                , 20070316, 20070615, 20070921, 20071221
                , 20080321, 20080620, 20080919, 20081219
                , 20090320, 20090619, 20090918, 20091218
                , 20100319, 20100618, 20100917, 20101217
                , 20110318, 20110617, 20110916, 20111216
                , 20120316, 20120615, 20120921, 20121221
                , 20130315, 20130621, 20130920, 20131220
                , 20140321, 20140620, 20140919, 20141219
                , 20150320, 20150619, 20150918, 20151218
                , 20160318, 20160617, 20160916, 20161216
                , 20170317, 20170616, 20170915, 20171215
                , 20180316, 20180615, 20180921, 20181221
                , 20190315, 20190621, 20190920, 20191220
                , 20200320, 20200619, 20200918, 20201218
                , 20210319, 20210618, 20210917, 20211217
                , 20220318, 20220617, 20220916, 20221216
                , 20230317, 20230616, 20230915, 20231215
                , 20240315, 20240621, 20240920, 20241220
        };

        futuresExpiration(ymds);
    }


    public static void optionExpiration(){
        int [] ymds ={
                19900119, 19900216, 19900316, 19900420, 19900518, 19900615, 19900720, 19900817, 19900921, 19901019, 19901116, 19901221
                , 19910118, 19910215, 19910315, 19910419, 19910517, 19910621, 19910719, 19910816, 19910920, 19911018, 19911115, 19911220
                , 19920117, 19920221, 19920320, 19920417, 19920515, 19920619, 19920717, 19920821, 19920918, 19921016, 19921120, 19921218
                , 19930115, 19930219, 19930319, 19930416, 19930521, 19930618, 19930716, 19930820, 19930917, 19931015, 19931119, 19931217
                , 19940121, 19940218, 19940318, 19940415, 19940520, 19940617, 19940715, 19940819, 19940916, 19941021, 19941118, 19941216
                , 19950120, 19950217, 19950317, 19950421, 19950519, 19950616, 19950721, 19950818, 19950915, 19951020, 19951117, 19951215
                , 19960119, 19960216, 19960315, 19960419, 19960517, 19960621, 19960719, 19960816, 19960920, 19961018, 19961115, 19961220
                , 19970117, 19970221, 19970321, 19970418, 19970516, 19970620, 19970718, 19970815, 19970919, 19971017, 19971121, 19971219
                , 19980116, 19980220, 19980320, 19980417, 19980515, 19980619, 19980717, 19980821, 19980918, 19981016, 19981120, 19981218
                , 19990115, 19990219, 19990319, 19990416, 19990521, 19990618, 19990716, 19990820, 19990917, 19991015, 19991119, 19991217
                , 20000121, 20000218, 20000317, 20000421, 20000519, 20000616, 20000721, 20000818, 20000915, 20001020, 20001117, 20001215
                , 20010119, 20010216, 20010316, 20010420, 20010518, 20010615, 20010720, 20010817, 20010921, 20011019, 20011116, 20011221
                , 20020118, 20020215, 20020315, 20020419, 20020517, 20020621, 20020719, 20020816, 20020920, 20021018, 20021115, 20021220
                , 20030117, 20030221, 20030321, 20030418, 20030516, 20030620, 20030718, 20030815, 20030919, 20031017, 20031121, 20031219
                , 20040116, 20040220, 20040319, 20040416, 20040521, 20040618, 20040716, 20040820, 20040917, 20041015, 20041119, 20041217
                , 20050121, 20050218, 20050318, 20050415, 20050520, 20050617, 20050715, 20050819, 20050916, 20051021, 20051118, 20051216
                , 20060120, 20060217, 20060317, 20060421, 20060519, 20060616, 20060721, 20060818, 20060915, 20061020, 20061117, 20061215
                , 20070119, 20070216, 20070316, 20070420, 20070518, 20070615, 20070720, 20070817, 20070921, 20071019, 20071116, 20071221
                , 20080118, 20080215, 20080321, 20080418, 20080516, 20080620, 20080718, 20080815, 20080919, 20081017, 20081121, 20081219
                , 20090116, 20090220, 20090320, 20090417, 20090515, 20090619, 20090717, 20090821, 20090918, 20091016, 20091120, 20091218
                , 20100115, 20100219, 20100319, 20100416, 20100521, 20100618, 20100716, 20100820, 20100917, 20101015, 20101119, 20101217
                , 20110121, 20110218, 20110318, 20110415, 20110520, 20110617, 20110715, 20110819, 20110916, 20111021, 20111118, 20111216
                , 20120120, 20120217, 20120316, 20120420, 20120518, 20120615, 20120720, 20120817, 20120921, 20121019, 20121116, 20121221
                , 20130118, 20130215, 20130315, 20130419, 20130517, 20130621, 20130719, 20130816, 20130920, 20131018, 20131115, 20131220
                , 20140117, 20140221, 20140321, 20140418, 20140516, 20140620, 20140718, 20140815, 20140919, 20141017, 20141121, 20141219
                , 20150116, 20150220, 20150320, 20150417, 20150515, 20150619, 20150717, 20150821, 20150918, 20151016, 20151120, 20151218
                , 20160115, 20160219, 20160318, 20160415, 20160520, 20160617, 20160715, 20160819, 20160916, 20161021, 20161118, 20161216
                , 20170120, 20170217, 20170317, 20170421, 20170519, 20170616, 20170721, 20170818, 20170915, 20171020, 20171117, 20171215
                , 20180119, 20180216, 20180316, 20180420, 20180518, 20180615, 20180720, 20180817, 20180921, 20181019, 20181116, 20181221
                , 20190118, 20190215, 20190315, 20190419, 20190517, 20190621, 20190719, 20190816, 20190920, 20191018, 20191115, 20191220
                , 20200117, 20200221, 20200320, 20200417, 20200515, 20200619, 20200717, 20200821, 20200918, 20201016, 20201120, 20201218
                , 20210115, 20210219, 20210319, 20210416, 20210521, 20210618, 20210716, 20210820, 20210917, 20211015, 20211119, 20211217
                , 20220121, 20220218, 20220318, 20220415, 20220520, 20220617, 20220715, 20220819, 20220916, 20221021, 20221118, 20221216
                , 20230120, 20230217, 20230317, 20230421, 20230519, 20230616, 20230721, 20230818, 20230915, 20231020, 20231117, 20231215
                , 20240119, 20240216, 20240315, 20240419, 20240517, 20240621, 20240719, 20240816, 20240920, 20241018, 20241115, 20241220
        };

        optionExpiration(ymds);
    }

    public static void futuresExpiration( int [] ymds){
        String closeHm = TradingTimes.getCloseTimeHm(CountryCode.USA);

        String dateFormat = "yyyyMMdd hhmm";

        EventCalendar eventCalendar = new EventCalendar();

        eventCalendar.setCountry(CountryCode.USA.toString());
        eventCalendar.setUpdatedAt(System.currentTimeMillis());
        eventCalendar.setNameKo("미국 선물 만기일");
        eventCalendar.setNameEn("USA futures expiration");
        eventCalendar.setEventType("futures_expiration");

        for(int ymd : ymds){

            eventCalendar.setYmd(ymd);
            eventCalendar.setEventId(eventCalendar.getCountry() + "_" + eventCalendar.getEventType() +"_" +ymd);
            String timeText = eventCalendar.getYmd() +" " + closeHm;
            eventCalendar.setEventTime(Times.getTime(dateFormat, timeText, TradingTimes.USA_ZONE_ID));

            TradingJdbc.updateTimeCheck(eventCalendar);

        }
    }

    public static void optionExpiration(int [] ymds){
        String closeHm = TradingTimes.getCloseTimeHm(CountryCode.USA);

        String dateFormat = "yyyyMMdd hhmm";

        EventCalendar eventCalendar = new EventCalendar();

        eventCalendar.setCountry(CountryCode.USA.toString());
        eventCalendar.setUpdatedAt(System.currentTimeMillis());
        eventCalendar.setNameKo("미국 옵션 만기일");
        eventCalendar.setNameEn("USA option expiration");
        eventCalendar.setEventType("option_expiration");
        for(int ymd : ymds){
            eventCalendar.setYmd(ymd);
            eventCalendar.setEventId(eventCalendar.getCountry() + "_" + eventCalendar.getEventType() +"_" +ymd);
            String timeText = eventCalendar.getYmd() +" " + closeHm;
            eventCalendar.setEventTime(Times.getTime(dateFormat, timeText, TradingTimes.USA_ZONE_ID));
            TradingJdbc.updateTimeCheck(eventCalendar);

        }
    }

    public static void presidentialElection(){
        int [] ymds ={
                19921103, 19961105, 20001107, 20041102, 20081104, 20121106, 20161108, 20201103, 20241105
        };
        presidentialElection(ymds);
    }

    public static void presidentialElection(int [] ymds){
        EventCalendar eventCalendar = new EventCalendar();

        eventCalendar.setCountry(CountryCode.USA.toString());
        eventCalendar.setUpdatedAt(System.currentTimeMillis());
        eventCalendar.setNameKo("미국 대통령 선거일");
        eventCalendar.setNameEn("USA presidential election");
        eventCalendar.setEventType("presidential_election");
        for(int ymd : ymds){
            eventCalendar.setYmd(ymd);
            eventCalendar.setEventId(eventCalendar.getCountry() + "_" + eventCalendar.getEventType() +"_" +ymd);
            eventCalendar.setEventTime(YmdUtil.getTime(ymd, TradingTimes.USA_ZONE_ID));
            TradingJdbc.updateTimeCheck(eventCalendar);
        }
    }

//    National Assembly election

    public static void midtermElections() {
        int [] ymds = {
                19901106, 19941108, 19981103, 20021105, 20061107, 20101102, 20141104, 20181106, 20221108
        };
        midtermElections(ymds);
    }

    public static void midtermElections(int [] ymds){
        EventCalendar eventCalendar = new EventCalendar();

        eventCalendar.setCountry(CountryCode.USA.toString());
        eventCalendar.setUpdatedAt(System.currentTimeMillis());
        eventCalendar.setNameKo("미국 중간선거일");
        eventCalendar.setNameEn("USA midterm elections");
        eventCalendar.setEventType("midterm_election");
        for(int ymd : ymds){
            eventCalendar.setYmd(ymd);
            eventCalendar.setEventId(eventCalendar.getCountry() + "_" + eventCalendar.getEventType() +"_" +ymd);
            eventCalendar.setEventTime(YmdUtil.getTime(ymd, TradingTimes.USA_ZONE_ID));
            TradingJdbc.updateTimeCheck(eventCalendar);
        }
    }

    public static void main(String[] args) {
//        futuresExpiration();
//        optionExpiration();
//        presidentialElection();
        midtermElections();
    }

}
