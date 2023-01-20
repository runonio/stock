# 글로벌 주식 식별번호
주요 식별 값 <br>
- ISIN, CIK, CUSIP, SEDOL, FIGI
- 나라마다 형식이 모두 다르다. 문자를 쓰기도 하고 숫자만 쓰기도 한다. 자릿수가 고정된 경우도 있고 가변적인 경우도 있다.
- 공식 명칭이 있지는 않은 것 같다. 검색상으로 느끼기엔 티커, 심벌, 코드, 티커 심벌, 스톡 심벌, 종목코드, 종목번호 등 의미가 통하는 용어는 다 사용되는 것 같다. 그리고 다른 나라도 마찬가지인 것으로 보인다.
<br>
참조 블로그
<br>
https://xassets.tistory.com/648
# ISIN 
국제 증권 식별 번호, International Securities Identification Number

- 위키피디아 : https://en.wikipedia.org/wiki/International_Securities_Identification_Number
- 공식 사이트 (등록 필요) : https://www.isin.org/
- 용도 : 할당된 번호를 사용해 표준화하는 것을 통해, 증권을 거래나 결제할 때 균일하게 식별하기 위한 용도로 만들어졌다. 주식, 채권, 옵션, 선물, 워런트, 권리, 화폐, 상품, 지수, 금리 등 거의 거래 가능한 것에 할당 가능
- 형식 : 12 자리 영숫자 코드
  - 2 자리 국가 코드
  - 9 자리 영숫자 증권 식별 코드 ( CUSIP에서 확장한 것과 SEDOL에서 확장한 것이 있음 )
  - 1 자리 체크 숫자 
  - 예제 : 애플 US0378331005

- 우리나라도 ISIN을 도입했으며 종목코드가 005930인 삼성전자의 ISIN은 'KR7005930003'이다. 두 코드에 공통된 부분이 있기는 하지만 실제로는 축약된 자리가 있어서 종목마다 별도로 확인해야 된다. 아래 내용 참고.
- 우리나라의 종목코드 관련 내용 기사 : https://biz.chosun.com/site/data/html_dir/2010/10/16/2010101600309.html
- 조회 방법 : 공식 사이트에서의 조회, 등록은 가입이 필요한 것으로 보인다. 구글에서 티커명 또는 기업명 뒤에 키워드 isin 을 추가해서 검색하면 검색 결과 중에 나오는 경우가 많다. 예) google search "microsoft isin"

# CIK
Central Index Key

- 위키피디아 : https://en.wikipedia.org/wiki/Central_Index_Key
- 공식 사이트 : https://www.sec.gov/
- 관련 기관 : SEC 미국 증권거래위원회 (U.S. Securities and Exchange Commission)
- 지역 : 미국
- 용도 : SEC에 보고서를 제출하는 회사를 식별하기 위해 사용하는 번호다. 따라서 미국 증권 거래소에서 거래되는 증권에서만 사용된다.
- 형식 : 최대 10 자리의 숫자 ( 검색 시 앞자리를 '0'으로 채워 10 자리로 사용해도 되고 안 해도 됨 )
- CIK를 몰라도 SEC의 EDGAR에서 회사의 이름이나 심벌을 통해 검색이 가능하지만, CIK를 이용하면 좀 더 유용하게 활용할 수 있음.

# CUSIP
Committee on Uniform Security Identification Procedures

- 위키피디아 : https://en.wikipedia.org/wiki/CUSIP
- 공식 사이트 : https://www.cusip.com/index.html
- 지역 : 북미 (미국, 캐나다)
- 관련 기업 : S&P Global Inc.
- 용도 : 거래의 투명성과 합의를 촉진하려는 목적으로 북미의 금융 증권을 식별하기 위해 만들어졌다. 기업, 정부, 지자체와 상장증권, 우선주, 펀드, 예금, 대출, 미국 캐나다에 등록된 옵션 등 국제증권을 커버한다.
- 형식 : 9 자리 영숫자 코드
  - 6 자리 발행자 코드
  - 2 자리 발행 숫자
  - 1 자리 체크 숫자
  - 예제 : 월마트 931142103
- CUSIP의 앞에 2 자리 국가코드는 추가하고, 뒤에 체크 숫자를 추가하면 ISIN 코드가 된다.

# SEDOL
Stock Exchange Daily Official List

- 위키피디아 : https://en.wikipedia.org/wiki/SEDOL
- 공식 사이트 (등록 필요) : https://www.sedol.co.uk/
- 지역 : 영국, 아일랜드
- 관련 기업 : 런던증권거래소 London Stock Exchange
- 용도 : 영국, 아일랜드에서 발행되는 증권의 식별자를 정리하기 위해서 만들어졌고, 런던 증권거래소에서 코드를 할당한다.
- 형식 : 7 자리 영숫자 코드
  - 6 자리 발행 코드
  - 1 자리 체크 숫자
  - 예제 : 월마트 2936921
- SEDOL의 앞에 2 자리 국가코드와 '00'을 추가하고, 뒤에 체크 숫자를 추가하면 ISIN 코드가 된다.

# FIGI 
Financial Instrument Global Identifier

- 예전 이름 : BBGID (Bloomberg Global Identifier)
- 위키피디아 : https://en.wikipedia.org/wiki/Financial_Instrument_Global_Identifier
- 공식 사이트 : https://www.openfigi.com/
- 관련 기업 : 블룸버그 Bloomberg L.P.
- 용도 : 금융상품의 고유식별자로 사용하기 위해 만들어졌다. 보통 주식, 옵션, 파생상품, 선물, 회사채 및 정부채, 지방채, 통화 및 주택담보대출 상품을 포함한 금융상품에 할당될 수 있다.
- 형식 : 12 자리 영숫자 코드
  - 2 자리 'BS, BM, GG, GB, GH, KY, VG'를 제외한 영문 대문자 자음
  - ( ISIN 코드와 시각적으로도 최대한 구분하기 위한 의도 )
  - 1 자리 대문자 'G'
  - 8 자리 영문 대문자 자음과 숫자 0-9의 조합
  - 1 자리 체크 숫자
  - 예제 : 아마존 BBG000BVPV84
- FIGI에는 할당 레벨이 있는데, 같은 증권이라도 글로벌 레벨, 국가 레벨, 거래소 레벨 단위로 다른 코드가 할당됨. 정확히 이해한 것인지는 모르겠는데, 상위 레벨로 할당된 코드를 통해 하위 레벨로 할당된 코드가 지칭하는 증권도 포함해서 지칭할 수 있는 것 같다. 예를 들어 '애플'은 미국의 14개 거래소에서 거래된다고 하고, 각 거래소에서 거래되는 애플 주식에는 Exchange FIGI가 할당되어 있고, 거래소와 상관없이 미국에서 거래되는 애플 주식을 구분할 때는 Composite FIGI를 이용하는 식인 것으로 보인다. 자세하게 찾아본 것은 아니지만, 일반 투자자라면 Composite FIGI만 구분해서 쓰면 될 것 같다.
  - Global Share Class Level : 글로벌 단위
  - Country Composite Level : 국가 단위
  - Exchange Level : 거래소 단위
- 검색을 통해 종합해 보면 FIGI는 기존 식별코드 사용에 들어가는 수수료와 ISIN 코드가 없는 증권의 존재 때문에 블룸버그의 주도하에 만든 것으로 보인다. 그러한 이유로 FIGI 관련 데이터는 무료로 사용할 수 있도록 공개되어 있고, 미리 FIGI 코드를 할당받지 않은 경우에도 할당 규칙만 맞다면 등록을 요청할 수 있다.