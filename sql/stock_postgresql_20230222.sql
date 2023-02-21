CREATE TABLE bonds
(
    bond_id              VARCHAR NOT NULL,
    country              VARCHAR NOT NULL,
    maturity             VARCHAR NOT NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);



ALTER TABLE bonds
    ADD PRIMARY KEY (bond_id);



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



CREATE TABLE futures
(
    futures_id           VARCHAR NOT NULL,
    futures_type         VARCHAR NOT NULL DEFAULT 'INDEX',
    exchange             VARCHAR NULL,
    tick_size            VARCHAR NULL,
    tick_value           VARCHAR NULL,
    symbol               VARCHAR NULL,
    point_value          VARCHAR NULL,
    maturity_month       VARCHAR NULL,
    contract_size        VARCHAR NULL,
    settlement_type      VARCHAR NULL,
    settlement_day       VARCHAR NULL,
    last_rollover_day    VARCHAR NULL
);



ALTER TABLE futures
    ADD PRIMARY KEY (futures_id);



CREATE TABLE indices
(
    index_id             VARCHAR NOT NULL,
    country              VARCHAR NOT NULL,
    group_id             VARCHAR NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);



ALTER TABLE indices
    ADD PRIMARY KEY (index_id);




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
    ADD PRIMARY KEY (group_id);



CREATE TABLE stock_group_map
(
    stock_group_id             VARCHAR NOT NULL,
    stock_id             VARCHAR NOT NULL,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);



ALTER TABLE stock_group_map
    ADD PRIMARY KEY (stock_group_id,stock_id);



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
    founding_ymd         INTEGER NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE stock
    ADD PRIMARY KEY (stock_id);


create index idx_stock_01
    on stock (exchange desc);

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
    PRIMARY KEY (row_no)
);



comment on table issue_shares_history is '주식발행이력';
        comment on column issue_shares_history.row_no is '로우번호';
         comment on column issue_shares_history.stock_id is '주식아이디';
         comment on column issue_shares_history.stock_type is '주식종류';
         comment on column issue_shares_history.par_value is '액면가';
         comment on column issue_shares_history.description is 'description';
         comment on column issue_shares_history.issue_qty is '발행주식수증감';
         comment on column issue_shares_history.issue_ymd is '발행년월일';
         comment on column issue_shares_history.listing_ymd is '상장년월일';

CREATE TABLE stock_daily
(
    stock_id             VARCHAR NOT NULL,
    ymd                  INTEGER NOT NULL,
    data_key             VARCHAR NOT NULL,
    data_value           VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (stock_id,ymd,data_key)
);



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



comment on table stock_api_data is '주식API데이터';
        comment on column stock_api_data.stock_id is '주식아이디';
         comment on column stock_api_data.api_url is 'api_url';
         comment on column stock_api_data.api_param is 'api_param';
         comment on column stock_api_data.data_value is '데이터값';
         comment on column stock_api_data.updated_at is '업데이트일시';




comment on table bonds is '채권';
        comment on column bonds.bond_id is '채권아이디';
         comment on column bonds.country is '국가';
         comment on column bonds.maturity is '채권만기';
         comment on column bonds.name_ko is '이름_한글';
         comment on column bonds.name_en is '이름_영문';
         comment on column bonds.description is 'description';
         comment on column bonds.updated_at is '업데이트일시';

comment on table exchange is '거래소';
        comment on column exchange.exchange is '거래소';
         comment on column exchange.country is '국가';
         comment on column exchange.currency is '기준통화';
         comment on column exchange.name_ko is '이름_한글';
         comment on column exchange.name_en is '이름_영문';
         comment on column exchange.description is 'description';
         comment on column exchange.updated_at is '업데이트일시';

comment on table futures is '선물';
        comment on column futures.futures_id is '선물아이디';
         comment on column futures.futures_type is '선물유형';
         comment on column futures.exchange is '거래소';
         comment on column futures.tick_size is '틱크기';
         comment on column futures.tick_value is '틱가치';
         comment on column futures.symbol is '티커_심볼';
         comment on column futures.point_value is '포인트가치';
         comment on column futures.maturity_month is '만기월';
         comment on column futures.contract_size is '계약단위';
         comment on column futures.settlement_type is '결제방식';
         comment on column futures.settlement_day is '결제일';
         comment on column futures.last_rollover_day is '최종롤오버일';

comment on table indices is '지수';
        comment on column indices.index_id is '지수아이디';
         comment on column indices.country is '국가';
         comment on column indices.stock_group_id is '그룹아이디';
         comment on column indices.name_ko is '이름_한글';
         comment on column indices.name_en is '이름_영문';
         comment on column indices.description is 'description';
         comment on column indices.updated_at is '업데이트일시';


comment on table stock_group is '주식그룹';
        comment on column stock_group.group_id is '그룹아이디';
         comment on column stock_group.group_type is '그륩유형';
         comment on column stock_group.country is '국가';
         comment on column stock_group.name_ko is '이름_한글';
         comment on column stock_group.name_en is '이름_영문';
         comment on column stock_group.description is 'description';
         comment on column stock_group.updated_at is '업데이트일시';

comment on table stock_group_map is '주식그룹맵';
        comment on column stock_group_map.stock_group_id is '그룹아이디';
         comment on column stock_group_map.stock_id is '주식아이디';
         comment on column stock_group_map.created_at is '등록일시';


create sequence seq_issue_shares_history;



