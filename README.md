# stock
주식 매매 프로젝트

주식 매매 자동화를 위한 프로젝트

- https://github.com/seomse/seomse-stock

초기시작은 위프로젝트에서 시작되었으며 새로 리뉴얼 합니다.

# stock-data

주식 분석에 필요한 데이터룰 내려받은 파일을 활용하는 형태와 Public Api 를 활용하는 유틸성 프로젝트 입니다.

데이터를 분석에 필요한 객체들로 변환시켜주는 작업, 데이터를 빠르게 읽어오는 작업 등이 해당됩니다.

# stock-ds
stock data service

- Relational Database Service
- Redis
- 기타 빅데이터 처리 시스템

위 서비스들은 사용자 단에서 직접 접근하는 경우는 없습니다. 직접 접근이 가능한 관련도가 깊은 개발자들이 사용하는 프로젝트이며 분석에 필요한 표준화된 데이터 구조로 변환시키는 작업을 합니다

# 데이터 구조

퀀트에 필요하다고 생각하는 데이터구조를 정리 합니다.
<br>
데이터 구조는 데이터를 다운받으면 서버에 접근없이 개별 PC 환경에서도 분석이 가능한게 목적에 있습니다.

<br><br>
현물과 선물의 폴더 구조는 국가코드_심볼
<br> 예:)KOR_500069

- data/stock/indices
  - 인덱스( 코스피, 나스닥, S&P500 ...)
- data/stock/spot
  - 현물정보 candle,order_book, 기타정보(신용, 대차잔고, 공매도)
  - 예졔: data/stock/spot/candle/KOR_500069
- data/stock/futures
  - 선물 정보 candle, order_book, open_interest 
  - data/stock/futures/indices 증시 선물
  - data/stock/futures/commodities 원자재 선물
  - data/stock/futures/bonds 채권 선물
- data/stock/company
  - 회사정보, 제무재표, 발행주식수, 공시
- data/bonds
  - 채권, 국채금리
- data/forex
  - 외환시장, 환율

- 매크로
  - 통화량(M1,M2,M3) 
  - 기준금리
  - CPI, PCE
  - OECD 경기선행지수
  - ISM 제조업 지표
  - 제조업 PMI
  - 개인지출
  - 실업률과 고용지표 관련자료 ADP고용
  - 실업청구건수 (고용지표 선행지수)
  - 구인건수, 자발적 퇴직건수
  - GDP
  - 국가부채 (GDP 대비 비율필요)
  - 가계부채 (GDP 대비 비율필요)
  - 회사부채 (GDP 대비 비율필요)
  - ECB Balance sheet (유럽 양적와화 긴축 차트)
  - US M2 money supply: YoY change (미국 유동성 차트)
  - 연준지수 (몇개의 주가 좋지 않은지)
  - GWIM equity allocation  (Bofa investment strategy)(주식비중 차트)
  - 연체율
    -  
  - 내부자지수 (insider transactions ratio)
  - 세계 중앙은행 자산규모 
    - 자산규모가 많아지면 시장 유동성은 증가 
- 부동산
  - 주택구입 부담지수
  - GDP 대비 부동산 시가총액

<br>
주식 종류와 유증, 액면 분할정보는 Sqlite 파일로 제공

# 데이터조사

컨센서스 수집 사이트 조사

# 데이터키
주식대차(대주정보): stock_loan

# communication
### blog, homepage
- [runon.io](https://runon.io)
- [www.seomse.com](https://www.seomse.com/)
- [github.com/runonio](https://github.com/runonio)
- [github.com/seomse](https://github.com/seomse)

### 카카오톡 오픈톡
 - https://open.kakao.com/o/g6vzOKqb
     - 참여코드: runon
 
# main developer
 - macle
    -  [github.com/macle86](https://github.com/macle86)
    -  [macle.dev](https://macle.dev)