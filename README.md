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

- data/indices/major
  - 세계주요지수
- data/indices/futures
  - 지수선물
- data/stock/spot
  - 현물정보 candle,order_book, 기타정보(신용, 대차잔고, 공매도)
  - 예졔: data/stock/spot/candle/KOR_500069
- data/stock/futures
  - 개별주식 선물 정보선물 정보 candle, order_book, open_interest 
  - data/stock/company
  - 회사정보, 제무재표, 발행주식수, 공시
- data/bonds/yield
  - 채권금리
- data/bonds/futures
  - 채권선물
- data/commodities/
  - 원자재 선물 
- data/currencies
  - 환율

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
  - 노동참여율
  - 임금상승률
    - 시간당 평균임금 상승률 
  - GDP
  - 국가부채 (GDP 대비 비율필요)
  - 가계부채 (GDP 대비 비율필요)
  - 회사부채 (GDP 대비 비율필요)
  - ECB Balance sheet (유럽 양적와화 긴축 차트)
  - US M2 money supply: YoY change (미국 유동성 차트)
  - 연준지수 (몇개의 주가 좋지 않은지)
  - GWIM equity allocation  (Bofa investment strategy)(주식비중 차트)
  - 연체율
  - 내부자지수 (insider transactions ratio)
  - 세계 중앙은행 자산규모 
    - 자산규모가 많아지면 시장 유동성은 증가 
  - fear-and-greed 
    - https://edition.cnn.com/markets/fear-and-greed
  - 신용 스프레드
  - Bears Reload On Equity Shorts (월가 숏교모, 소수몽키가 Bloomberg 자료에서 발췌 ,Source: CFTC)
  - 연준 자산
    - 자산이 증가하면 연준이 돈을 풀고 있다는 의미 
  - 전세계 성장률 (미국, 한국 우선순위) 
  - IBES
    - 1976년부터 다양한 애널리스트 전망자료  
  - 컨퍼런스보드 경기선행지수 
  - 미국 소매판매증가율
  - 미국 소매판매 로그차트 (우상향) 인플레감안 없음 인플레 + 인구증가로 이론상 우상향 해야하는데 꺽이면 해석필요
  - 컨퍼런스보드 경기선행지수
  - 연준의 은행 긴급대출 자금 차트
  - 역레포시장

- 부동산
  - 주택구입 부담지수
  - GDP 대비 부동산 시가총액
  - PIR 가구소득 대비 주택가격비율 (주택구입까지 몇년동안 번 소득으로 살 수 있는지)
    - 전국가, 각 국가의 부동산 고평가 저평가  


<br>
주식 종류와 유증, 액면 분할정보는 Sqlite 파일로 제공

- 유상증좌
  - 운영자금 유상증좌는 최악의 시나리오
  - 사업이 잘되서 장비증설을 위한 유상증좌는 나쁜시나리오는 아님. (상황에 맞게 분석)

# 사업보고서
- 신규 투자 항목, 투자툭소 항목 ( 아직 )
  - 하이리스크 하이리턴
  - 벨류 체인에 들어나지 않을때 투자하는 방법
- 새로 등장한 매출 구성제품 분석
  - 주 매출 구성 제품은 가격에 반영되어있는 경우가 많음
  - 새로운 제품중에 매출의 증가가 커지는 제품이 있는경우 시장분석


# 데이터조사
- 자사주 매입신청 페이지
  - https://kind.krx.co.kr/corpgeneral/treasurystk.do?method=loadInitPage
컨센서스 수집 사이트 조사

# 데이터키
주식대차(대주정보): stock_loan

# communication
### blog, homepage
- [github.com/runonio](https://github.com/runonio)
- [runon.io](https://runon.io)
- [github.com/seomse](https://github.com/seomse)
- [www.seomse.com](https://www.seomse.com/)


### email
- iorunon@gmail.com

### cafe
- [cafe.naver.com/radvisor](https://cafe.naver.com/radvisor)

## main developer
- macle
  - github(source code): [github.com/macle86](https://github.com/macle86)
  - email: ysys86a@gmail.com