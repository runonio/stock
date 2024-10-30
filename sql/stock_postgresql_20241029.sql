

CREATE TABLE exchange
(
    exchange             VARCHAR NOT NULL,
    country              VARCHAR NOT NULL,
    currency             VARCHAR NOT NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);



ALTER TABLE exchange
    ADD PRIMARY KEY (exchange);

create index idx_exchange_01
    on exchange (updated_at desc);



CREATE TABLE stock_group
(
    stock_group_id             VARCHAR NOT NULL,
    group_type           VARCHAR NOT NULL,
    country              VARCHAR NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);



ALTER TABLE stock_group
    ADD PRIMARY KEY (stock_group_id);

create index idx_stock_group_01
    on stock_group (updated_at desc);

CREATE TABLE stock_group_map
(
    stock_group_id             VARCHAR NOT NULL,
    stock_id             VARCHAR NOT NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE stock_group_map
    ADD PRIMARY KEY (stock_group_id,stock_id);


create index idx_stock_group_map_01
    on stock_group_map (updated_at desc);



CREATE TABLE stock
(
    stock_id             VARCHAR NOT NULL,
    exchange             VARCHAR NULL,
    symbol               VARCHAR NULL,
    stock_type           VARCHAR NOT NULL DEFAULT 'STOCK',
    isin                 VARCHAR NULL,
    cik                  VARCHAR NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    issued_shares        NUMERIC NULL,
    shares_outstanding   NUMERIC NULL,
    is_listing           boolean NOT NULL DEFAULT true,
    listed_ymd           INTEGER NULL,
    delisted_ymd         INTEGER NULL,
    delist_reason        VARCHAR NULL,
    founding_ymd         INTEGER NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE stock
    ADD PRIMARY KEY (stock_id);


create index idx_stock_01
    on stock (exchange desc);


create index idx_stock_02
    on stock (updated_at desc);


comment on table stock is '주식';
        comment on column stock.stock_id is '주식아이디';
         comment on column stock.exchange is '거래소';
         comment on column stock.symbol is '심볼';
         comment on column stock.stock_type is '주식유형';
         comment on column stock.isin is 'ISIN';
         comment on column stock.cik is 'CIK';
         comment on column stock.name_ko is '이름_한글';
         comment on column stock.name_en is '이름_영문';
         comment on column stock.description is 'description';
         comment on column stock.issued_shares is '발행주식수';
         comment on column stock.shares_outstanding is '유통주식수';
         comment on column stock.is_listing is '상장여부';
         comment on column stock.listed_ymd is '상장년월일';
         comment on column stock.delisted_ymd is '상장폐지년월일';
         comment on column stock.delist_reason is '상장폐지사유';
         comment on column stock.founding_ymd is '창립년월일';
         comment on column stock.updated_at is '업데이트일시';


CREATE TABLE issue_shares_history
(
    row_no               BIGINT NOT NULL,
    stock_id             VARCHAR NOT NULL,
    stock_type           VARCHAR NOT NULL DEFAULT 'STOCK',
    par_value            NUMERIC NULL,
    description          VARCHAR NULL,
    issue_qty            NUMERIC NOT NULL,
    issue_ymd            INTEGER NULL,
    listing_ymd          INTEGER NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (row_no)
);

create index idx_issue_shares_history_01
    on issue_shares_history (updated_at desc);

comment on table issue_shares_history is '주식발행이력';
        comment on column issue_shares_history.row_no is '로우번호';
         comment on column issue_shares_history.stock_id is '주식아이디';
         comment on column issue_shares_history.stock_type is '주식종류';
         comment on column issue_shares_history.par_value is '액면가';
         comment on column issue_shares_history.description is 'description';
         comment on column issue_shares_history.issue_qty is '발행주식수증감';
         comment on column issue_shares_history.issue_ymd is '발행년월일';
         comment on column issue_shares_history.listing_ymd is '상장년월일';
         comment on column issue_shares_history.updated_at is '업데이트일시';

create sequence seq_issue_shares_history;

CREATE TABLE stock_daily
(
    stock_id             VARCHAR NOT NULL,
    ymd                  INTEGER NOT NULL,
    data_key             VARCHAR NOT NULL,
    data_value           VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (stock_id,ymd,data_key)
);


create index idx_stock_daily_01
    on stock_daily (updated_at desc);


create index idx_stock_daily_02
    on stock_daily (ymd desc);


create index idx_stock_daily_03
    on stock_daily (data_key, ymd desc);



comment on table stock_daily is '주식일별데이터';
        comment on column stock_daily.stock_id is '주식아이디';
         comment on column stock_daily.ymd is '년월일';
         comment on column stock_daily.data_key is '데이터키';
         comment on column stock_daily.data_value is '데이터값';
         comment on column stock_daily.updated_at is '업데이트일시';

CREATE TABLE stock_api_data
(
    stock_id             VARCHAR NOT NULL,
    api_url              VARCHAR NOT NULL,
    api_param            VARCHAR NOT NULL,
    data_value           VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (stock_id,api_url,api_param)
);


create index idx_stock_api_data_01
    on stock_api_data (updated_at desc);

comment on table stock_api_data is '주식API데이터';
        comment on column stock_api_data.stock_id is '주식아이디';
         comment on column stock_api_data.api_url is 'api_url';
         comment on column stock_api_data.api_param is 'api_param';
         comment on column stock_api_data.data_value is '데이터값';
         comment on column stock_api_data.updated_at is '업데이트일시';


comment on table exchange is '거래소';
        comment on column exchange.exchange is '거래소';
         comment on column exchange.country is '국가';
         comment on column exchange.currency is '기준통화';
         comment on column exchange.name_ko is '이름_한글';
         comment on column exchange.name_en is '이름_영문';
         comment on column exchange.description is 'description';
         comment on column exchange.updated_at is '업데이트일시';


comment on table stock_group is '주식그룹';
        comment on column stock_group.stock_group_id is '주식그룹아이디';
         comment on column stock_group.group_type is '그륩유형';
         comment on column stock_group.country is '국가';
         comment on column stock_group.name_ko is '이름_한글';
         comment on column stock_group.name_en is '이름_영문';
         comment on column stock_group.description is 'description';
         comment on column stock_group.updated_at is '업데이트일시';

comment on table stock_group_map is '주식그룹맵';
        comment on column stock_group_map.stock_group_id is '그룹아이디';
         comment on column stock_group_map.stock_id is '주식아이디';
         comment on column stock_group_map.updated_at is '업데이트일시';



CREATE TABLE bonds
(
    bond_id              VARCHAR NOT NULL,
    country              VARCHAR NOT NULL,
    maturity             VARCHAR NOT NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    candle_path          VARCHAR NULL,
    description          VARCHAR NULL,
    data_value           VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (bond_id)
);


create index idx_bonds_01
    on bonds (updated_at desc);


CREATE TABLE indices
(
    index_id             VARCHAR NOT NULL,
    country              VARCHAR NOT NULL,
    stock_group_id       VARCHAR NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    candle_path          VARCHAR NULL,
    description          VARCHAR NULL,
    data_value           VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (index_id)
);

create index idx_indices_01
    on indices (updated_at desc);



comment on table bonds is '채권';
        comment on column bonds.bond_id is '채권아이디';
         comment on column bonds.country is '국가';
         comment on column bonds.maturity is '채권만기';
         comment on column bonds.name_ko is '이름_한글';
         comment on column bonds.name_en is '이름_영문';
         comment on column bonds.candle_path is '캔들경로';
         comment on column bonds.description is 'description';
         comment on column bonds.data_value is '데이터값';
         comment on column bonds.updated_at is '업데이트일시';

comment on table indices is '지수';
        comment on column indices.index_id is '지수아이디';
         comment on column indices.country is '국가';
         comment on column indices.stock_group_id is '그룹아이디';
         comment on column indices.name_ko is '이름_한글';
         comment on column indices.name_en is '이름_영문';
         comment on column indices.candle_path is '캔들경로';
         comment on column indices.description is 'description';
         comment on column indices.data_value is '데이터값';
         comment on column indices.updated_at is '업데이트일시';


CREATE TABLE futures
(
    futures_id              VARCHAR NOT NULL,
    underlying_assets_type  VARCHAR NOT NULL DEFAULT 'INDEX',
    exchange                VARCHAR NULL,
    name_ko                 VARCHAR NULL,
    name_en                 VARCHAR NULL,
    candle_path             VARCHAR NULL,
    currency                VARCHAR NULL,
    underlying_assets_id    VARCHAR NULL,
    product_type            VARCHAR NULL,
    symbol                  VARCHAR NULL,
    standard_code           VARCHAR NULL,
    listed_ymd             INTEGER NULL,
    last_trading_ymd        INTEGER NULL,
    settlement_ymd          INTEGER NULL,
    trade_multiplier        NUMERIC NULL,
    description             VARCHAR NULL,
    data_value              VARCHAR NULL,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (futures_id)
);

create index idx_futures_01
    on futures (updated_at desc);


create index idx_futures_02
    on futures (standard_code);


create index idx_futures_03
    on futures (symbol);


comment on table futures is '선물';
        comment on column futures.futures_id is '선물아이디';
         comment on column futures.underlying_assets_type is '기초자산유형';
         comment on column futures.exchange is '거래소';
         comment on column futures.name_ko is '이름_한글';
         comment on column futures.name_en is '이름_영문';
         comment on column futures.candle_path is '캔들경로';
         comment on column futures.currency is '기준통화';
         comment on column futures.underlying_assets_id is '기초자산아이디';
         comment on column futures.product_type is '상품유형';
         comment on column futures.symbol is '티커_심볼';
         comment on column futures.standard_code is '표준코드';
         comment on column futures.listed_ymd is '상장년월일';
         comment on column futures.last_trading_ymd is '최종거래일';
         comment on column futures.settlement_ymd is '결제일';
         comment on column futures.trade_multiplier is '거래승수';
         comment on column futures.description is 'description';
         comment on column futures.data_value is '데이터값';
         comment on column futures.updated_at is '업데이트일시';

CREATE TABLE currencies
(
    currency_id          VARCHAR NOT NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    candle_path          VARCHAR NULL,
    description          VARCHAR NULL,
    data_value           VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (currency_id)
);

create index idx_currencies_01
    on currencies (updated_at desc);


comment on table currencies is '통화_환율';
        comment on column currencies.currency_id is '통화아이디';
         comment on column currencies.name_ko is '이름_한글';
         comment on column currencies.name_en is '이름_영문';
         comment on column currencies.candle_path is '캔들경로';
         comment on column currencies.description is 'description';
         comment on column currencies.data_value is '데이터값';
         comment on column currencies.updated_at is '업데이트일시';


CREATE TABLE daily_data
(
    data_key             VARCHAR NOT NULL,
    ymd                  INTEGER NOT NULL,
    data_value           VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (data_key,ymd)
);

create index idx_daily_data_01
    on daily_data (updated_at desc);


create index idx_daily_data_02
    on daily_data (ymd desc);


comment on table daily_data is '일별데이터';
        comment on column daily_data.data_key is '데이터키';
         comment on column daily_data.ymd is '년월일';
         comment on column daily_data.data_value is '데이터값';
         comment on column daily_data.updated_at is '업데이트일시';

CREATE TABLE category
(
    category_id          VARCHAR NOT NULL,
    category_type        VARCHAR NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    is_del               boolean NOT NULL DEFAULT false,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (category_id)
);


create index idx_category_01
    on category (updated_at desc);

CREATE TABLE category_code
(
    category_id          VARCHAR NOT NULL,
    code                 VARCHAR NOT NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    is_del               boolean NOT NULL DEFAULT false,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (category_id,code)
);

create index idx_category_code_01
    on category_code (updated_at desc);


CREATE TABLE common_config
(
    config_key           VARCHAR NOT NULL,
    config_value         VARCHAR NULL,
    description          VARCHAR NULL,
    is_del               boolean NOT NULL DEFAULT false,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (config_key)
);

create index idx_common_config_01
    on common_config (updated_at desc);





comment on table category is '카테고리';
        comment on column category.category_id is '카테고리아이디';
         comment on column category.category_type is '카테고리유형';
         comment on column category.name_ko is '이름_한글';
         comment on column category.name_en is '이름_영문';
         comment on column category.description is 'description';
         comment on column category.is_del is '삭제여부';
         comment on column category.updated_at is '업데이트일시';

comment on table category_code is '카테고리코드';
        comment on column category_code.category_id is '카테고리아이디';
         comment on column category_code.code is '코드';
         comment on column category_code.name_ko is '이름_한글';
         comment on column category_code.name_en is '이름_영문';
         comment on column category_code.description is 'description';
         comment on column category_code.is_del is '삭제여부';
         comment on column category_code.updated_at is '업데이트일시';

comment on table common_config is '공통설정';
        comment on column common_config.config_key is '설정키';
         comment on column common_config.config_value is '설정값';
         comment on column common_config.description is 'description';
         comment on column common_config.is_del is '삭제여부';
         comment on column common_config.updated_at is '업데이트일시';




CREATE TABLE event_calendar
(
    event_id             VARCHAR NOT NULL,
    event_time           TIMESTAMP NOT NULL,
    ymd                  INTEGER NULL,
    event_type           VARCHAR NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    country              VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (event_id)
);


create index idx_event_calendar_01
    on event_calendar (updated_at desc);

create index idx_event_calendar_02
    on event_calendar (event_time desc);

create index idx_event_calendar_03
    on event_calendar (ymd desc);


CREATE TABLE event_calendar_item
(
    event_id             VARCHAR NOT NULL,
    item_type            VARCHAR NOT NULL,
    item_id              VARCHAR NOT NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (event_id,item_type,item_id)
);
create index idx_event_calendar_item_01
    on event_calendar_item (updated_at desc);


comment on table event_calendar is '이벤트캘린더';
        comment on column event_calendar.event_id is '이벤트아이디';
         comment on column event_calendar.event_time is '이벤트시간';
         comment on column event_calendar.ymd is '년월일';
         comment on column event_calendar.event_type is '이벤트유형';
         comment on column event_calendar.name_ko is '이름_한글';
         comment on column event_calendar.name_en is '이름_영문';
         comment on column event_calendar.description is 'description';
         comment on column event_calendar.country is '국가';
         comment on column event_calendar.updated_at is '업데이트일시';

comment on table event_calendar_item is '이벤트캘린더영향종목';
        comment on column event_calendar_item.event_id is '이벤트아이디';
         comment on column event_calendar_item.item_type is '종목유형';
         comment on column event_calendar_item.item_id is '종목아이디';
         comment on column event_calendar_item.updated_at is '업데이트일시';


CREATE TABLE time_text
(
    data_key             VARCHAR NOT NULL,
    time_long            BIGINT NOT NULL,
    data_value           VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (data_key,time_long)
);


comment on table time_text is '타임텍스트';
        comment on column time_text.data_key is '데이터키';
         comment on column time_text.time_long is '타임_long';
         comment on column time_text.data_value is '데이터값';
         comment on column time_text.updated_at is '업데이트일시';

create index idx_time_text_01
    on time_data (updated_at desc);



CREATE TABLE category_key_value
(
    category_id          VARCHAR NOT NULL,
    data_key             VARCHAR NOT NULL,
    data_value           VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (category_id,data_key)
);



comment on table category_key_value is '카테고리key-value';
        comment on column category_key_value.category_id is '카테고리아이디';
         comment on column category_key_value.data_key is '데이터키';
         comment on column category_key_value.data_value is '데이터값';
         comment on column category_key_value.updated_at is '업데이트일시';


create index idx_category_key_value_01
    on category_key_value (updated_at desc);



