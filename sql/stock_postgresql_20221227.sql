-- 설계상에 더 많은 테이블이 존재하지만 당장에 바로 활용하는 테이블만 추가

CREATE TABLE stock
(
    stock_id             BIGINT NOT NULL,
    country              VARCHAR NULL,
    symbol               VARCHAR NULL,
    market               VARCHAR NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cik                  VARCHAR NULL,
    stock_type           VARCHAR NOT NULL,
    description          VARCHAR NULL,
    is_listing           boolean NOT NULL,
    founded_at           TIMESTAMP NULL,
    listed_at            TIMESTAMP NULL
);

CREATE UNIQUE INDEX stock_pkey ON stock
    (
     stock_id
        );


ALTER TABLE stock
    ADD PRIMARY KEY (stock_id);


comment on table stock is '주식종목';
        comment on column stock.stock_id is '주식아이디';
         comment on column stock.country is '국가코드';
         comment on column stock.market is '시장';
         comment on column stock.symbol is '심볼';
         comment on column stock.stock_type is '주식유형';
         comment on column stock.cik is 'CIK';
         comment on column stock.name_ko is '이름_한글';
         comment on column stock.name_en is '이름_영문';
         comment on column stock.description is 'description';
         comment on column stock.is_listing is '상장여부';
         comment on column stock.listed_at is '상장일시';
         comment on column stock.founded_at is '창립일시';
         comment on column stock.updated_at is '업데이트일시';




CREATE TABLE stock_group
(
    stock_group_id       BIGINT NOT NULL,
    group_type           VARCHAR NOT NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX stock_group_pkey ON stock_group
    (
     stock_group_id
        );

ALTER TABLE stock_group
    ADD PRIMARY KEY (stock_group_id);

CREATE TABLE stock_group_map
(
    stock_id             BIGINT NOT NULL,
    stock_group_id       BIGINT NOT NULL,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX stock_group_map_pkey ON stock_group_map
    (
     stock_id,
     stock_group_id
        );

ALTER TABLE stock_group_map
    ADD PRIMARY KEY (stock_id,stock_group_id);

CREATE TABLE stock_market
(
    market               VARCHAR NOT NULL,
    country              VARCHAR NULL,
    currency             VARCHAR NULL,
    exchange             VARCHAR NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NOT NULL,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX stock_market_pkey ON stock_market
    (
     market
        );

ALTER TABLE stock_market
    ADD PRIMARY KEY (market);

CREATE TABLE stock_market_map
(
    market               VARCHAR NOT NULL,
    stock_id             BIGINT NOT NULL,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX stock_market_map_pkey ON stock_market_map
    (
     market,
     stock_id
        );

ALTER TABLE stock_market_map
    ADD PRIMARY KEY (market,stock_id);


comment on table stock_group is '주식그룹'; 
        comment on column stock_group.stock_group_id is '주식그룹아이디'; 
         comment on column stock_group.group_type is '그륩유형'; 
         comment on column stock_group.name_ko is '이름_한글'; 
         comment on column stock_group.name_en is '이름_영문'; 
         comment on column stock_group.description is 'description'; 
         comment on column stock_group.created_at is '등록일시'; 
         comment on column stock_group.updated_at is '업데이트일시'; 
          
comment on table stock_group_map is '주식그룹맵'; 
        comment on column stock_group_map.stock_id is '주식아이디'; 
         comment on column stock_group_map.stock_group_id is '주식그룹아이디'; 
         comment on column stock_group_map.created_at is '등록일시'; 
          
comment on table stock_market is '주식시장[증시]'; 
        comment on column stock_market.market is '시장'; 
         comment on column stock_market.country is '국가코드'; 
         comment on column stock_market.currency is '기준통화'; 
         comment on column stock_market.exchange is '거래소'; 
         comment on column stock_market.name_ko is '이름_한글'; 
         comment on column stock_market.name_en is '이름_영문'; 
         comment on column stock_market.description is 'description'; 
         comment on column stock_market.created_at is '등록일시';
         comment on column stock_market.updated_at is '업데이트일시'; 
          
comment on table stock_market_map is '주식시장맵'; 
        comment on column stock_market_map.market is '시장'; 
         comment on column stock_market_map.stock_id is '주식아이디'; 
         comment on column stock_market_map.created_at is '등록일시';


CREATE SEQUENCE seq_stock START 1;
CREATE SEQUENCE seq_stock_group START 1;


