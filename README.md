# stock
주식 매매 프로젝트

주식 매매 자동화를 위한 프로젝트

- https://github.com/seomse/seomse-stock

초기시작은 위프로젝트에서 시작되었으며 새로 리뉴얼 합니다.

# 진행중
개별주식 리벨런싱 및 자동매매, 종가매매 

# 로드맵 (예정)

- 시장별매매동향
- 선물 옵션 매매동향
  - 선물옵션과 같이 분석해야 하지만 선물옵션 정보를 정확히 파악하기위한 사전 지식이 필요함
  - 선물옵션 코드는 어떻게 관리하는지 6월물 7월물등 월물이 변경되는것, 만기일 처리등에 대한 방안을 적립한후 적용

## 신용정보(일별) json 데이터
trade_ymd 매매일자
payment_ymd 결제일자
loan_new_count 융자 신규 주식수
loan_repayment_count 융자 상환 주식수
loan_balance_count 융자 잔고 주식수
loan_new_amount 융자 신규 금액
loan_repayment_amount 융자 상환 금액
loan_balance_amount 융자 잔고 금액
loan_balance_rate 융자 잔고 비율
loan_trade_rate 융자 공여율
close 종가
open 시가
high 고가
low 저가
volume 누적거래량


# 내제가치 계산법
EPS = ((최근년도 EPS *3) + (전년도 EPS *2) + (전전년도 EPS*1) )/6
(BPS + EPS*10) / 2



# Hidden Markov model
관련 모델 적용 연구해보기

https://ko.wikipedia.org/wiki/%EC%9D%80%EB%8B%89_%EB%A7%88%EB%A5%B4%EC%BD%94%ED%94%84_%EB%AA%A8%ED%98%95

https://en.wikipedia.org/wiki/Hidden_Markov_model

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
- data/stock/(국가코드)/spot/candle
  - 현물 캔들정보  
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

# 이후 추가 데이터 연동이 필요한 항목

- 매크로
  - 통화량(M1,M2,M3) 
    - 중앙은행 자산, 국가부채, 가계부채를 통합해서 봐야할거 같음. 
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

# 국내 공시정보 수집
dart api 공시정보 검색을 활용해서 검색된 공시 정부 수집하기.
주요공시 목록
- 배당공시
- 주식수 공시
- 주요주주 공시
- 유상증좌 결정
- 무상증좌 결정
- 감자결정
- 전환사채 발행결정
- 제무재표
- 자사주 취득
- 자사주 처분


# 데이터조사
- 자사주 매입신청 페이지
  - https://kind.krx.co.kr/corpgeneral/treasurystk.do?method=loadInitPage
컨센서스 수집 사이트 조사
- https://datamall.koscom.co.kr/kor/datamall/stock/middaySearchData.do?screenId=100130&menuNo=200014
  - 코스콤 데이터 판매가격 

# 데이터키
주식대차(대주정보): stock_loan

# 뉴스활용
뉴스에서 많이나오는 단어 기업내 주요인물을 이터뷰한 기사. 주요인물이 무엇을 중요하게 보고있는지를 보고 섹터를 파악.

역배열종목을 제외하고 정배열종목을 관찰

관련방법 소개영상

- https://www.youtube.com/watch?v=Ia4m0MW6MAs

# communication
### blog, homepage
- [runon.io](https://runon.io)
- [github.com/runonio](https://github.com/runonio)
- [github.com/seomse](https://github.com/seomse)
- [www.seomse.com](https://www.seomse.com/)


### email
- iorunon@gmail.com

### cafe
- [cafe.naver.com/radvisor](https://cafe.naver.com/radvisor)


### talk
- 로보어드바이저, 시스템트레이딩, 퀀트 단톡방
  - https://open.kakao.com/o/g6vzOKqb
  - 참여코드: runon

## main developer
- macle
  - github(source code): [github.com/macle86](https://github.com/macle86)
  - email: ysys86a@gmail.com