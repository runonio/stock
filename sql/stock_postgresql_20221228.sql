-- 설계상에 더 많은 테이블이 존재하지만 당장에 바로 활용하는 테이블만 추가
CREATE TABLE stock
(
    stock_id             BIGINT NOT NULL,
    country              VARCHAR NULL,
    exchange             VARCHAR NULL,
    symbol               VARCHAR NULL,
    stock_type           VARCHAR NOT NULL,
    cik                  VARCHAR NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    is_listing           boolean NOT NULL DEFAULT true,
    listed_at            TIMESTAMP NULL,
    founded_at           TIMESTAMP NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE stock
    ADD PRIMARY KEY (stock_id);

CREATE TABLE stock_exchange
(
    exchange             VARCHAR NOT NULL,
    country              VARCHAR NULL,
    currency             VARCHAR NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE stock_exchange
    ADD PRIMARY KEY (exchange);

CREATE TABLE stock_group
(
    group_symbol         VARCHAR NOT NULL,
    group_type           VARCHAR NOT NULL,
    name_ko              VARCHAR NULL,
    name_en              VARCHAR NULL,
    description          VARCHAR NULL,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE stock_group
    ADD PRIMARY KEY (group_symbol);

CREATE TABLE stock_group_map
(
    group_symbol         VARCHAR NOT NULL,
    stock_id             BIGINT NOT NULL,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE stock_group_map
    ADD PRIMARY KEY (group_symbol,stock_id);

comment on table stock is '주식종목';
        comment on column stock.stock_id is '주식아이디';
         comment on column stock.country is '국가코드';
         comment on column stock.exchange is '거래소';
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

comment on table stock_exchange is '거래소';
        comment on column stock_exchange.exchange is '거래소';
         comment on column stock_exchange.country is '국가코드';
         comment on column stock_exchange.currency is '기준통화';
         comment on column stock_exchange.name_ko is '이름_한글';
         comment on column stock_exchange.name_en is '이름_영문';
         comment on column stock_exchange.description is 'description';
         comment on column stock_exchange.updated_at is '업데이트일시';

comment on table stock_group is '주식그룹';
        comment on column stock_group.group_symbol is '주식그룹심볼';
         comment on column stock_group.group_type is '그륩유형';
         comment on column stock_group.name_ko is '이름_한글';
         comment on column stock_group.name_en is '이름_영문';
         comment on column stock_group.description is 'description';
         comment on column stock_group.updated_at is '업데이트일시';

comment on table stock_group_map is '주식그룹맵';
        comment on column stock_group_map.group_symbol is '주식그룹심볼';
         comment on column stock_group_map.stock_id is '주식아이디';
         comment on column stock_group_map.created_at is '등록일시';

CREATE SEQUENCE seq_stock START 1;