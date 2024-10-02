# 종목 토론방

- 종목토론방 API는 주식 투자자들이 실시간으로 종목에 대해 토론하고 정보를 공유할 수 있는 플랫폼을 제공합니다.

#  주식 데이터 초기화 관련 제한사항
<details><summary><strong>펼처보기</strong></summary>

##  기존 API 개요 및 제한사항

### 사이트 개요
| 항목 | 설명                            |
|:-----|:------------------------------|
|  목적 | 국내 상장 증권 토론 커뮤니티              |
|  제공 데이터 | • 5년간 일일 데이터<br>• 1주간 분단위 데이터 |

### API 정보
| 항목     | 상세 |
|:-------|:-----|
| 제공원    | 한국투자증권 공개 API |
|  사용 제한 | 초당 2회 |

### 데이터 초기화 요구사항 (1종목 기준)
| 데이터 유형        |   호출 횟수    | 비고 |
|:--------------|:----------:|:-----|
| 일일 데이터        |    ~12회    | 1회 호출 = 100일 데이터 |
|  분단위 데이터      |   1950회    | 1회 호출 = 1개 데이터 |
| **총 호출 횟수**   | **~1962회** | |

### 초기화 소요 시간
```
1종목 초기화 = 최소 17분
```

##  네이버 증권 API 사용 결정 배경
1. 초기화 시간 단축: 2번의 요청으로 한 종목 일간 및 분단위 데이터 초기화 가능
2. 성능 변수 최소화: 모든 종목에 대해 실제 데이터 사용으로 예측하지 못한 성능 차이 방지
3. 개발 및 테스트 효율성 증대

## 🔌 네이버 증권 API 구조

### 1. 일별 차트 데이터 URL
| 구성 요소 | 값 |
|-----------|-----|
| URL | https://fchart.stock.naver.com/sise.nhn |
| symbol | {종목코드} |
| timeframe | day |
| count | 오늘로부터 장이 열린 날의 횟수 |
| requestType | 0 |

완성된 URL 형태:
```
https://fchart.stock.naver.com/sise.nhn?symbol={종목코드}&timeframe=day&count={장이_열린_날_횟수}&requestType=0
```

### 2. 분 단위 차트 데이터 URL
| 구성 요소 | 값 |
|-----------|-----|
| URL | https://api.stock.naver.com/chart/domestic/item |
| symbol | {종목코드} |
| startDateTime | {시작일시} |
| endDateTime | {종료일시} |

완성된 URL 형태:
```
https://api.stock.naver.com/chart/domestic/item/{종목코드}/minute?startDateTime={시작일시}&endDateTime={종료일시}
```

- 네이버 증권 API를 사용함으로써 데이터 초기화 시간을 크게 단축하고, 실제 데이터를 사용하여 성능 테스트의 정확도를 높일 수 있습니다.
- 초기화 후 서버 운영 과정에서 모든 종목을 크롤링으로 매분 업데이트 하는것은 매우 민폐이므로 초기화를 한 뒤에는 거래량이 많은 10종목만 실시간으로 다루었다.
-

</details>


## 기술 스택

- Java 21
- Spring 3.3.2
- Spring Data JPA 3.3.2
- Spring Batch 3.3.2
- Spring Data Redis 3.3.2
- Spring Security 3.3.2
- Spring WebFlux 6.0.13
- JJWT 0.12.3
- Swagger v3 2.2.9

## 데이터베이스

- MySQL 8.0.33
- Redis 7.0.8
- AWS S3

## 아키텍쳐

![archtecture](https://private-user-images.githubusercontent.com/167910692/372846046-b2bde95c-cda1-4a87-9725-bfaa73453ea5.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mjc4NjkyMDQsIm5iZiI6MTcyNzg2ODkwNCwicGF0aCI6Ii8xNjc5MTA2OTIvMzcyODQ2MDQ2LWIyYmRlOTVjLWNkYTEtNGE4Ny05NzI1LWJmYWE3MzQ1M2VhNS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMDAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTAwMlQxMTM1MDRaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0xMGE4NmI1ZTJiZjI5YmJlNzUwZDc0YWVkOWM1MWVmY2FhY2YzNzlkMGI3ZDYyZmVkNDBjMDhjYTkyZTZhM2MzJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.kUOJIHdstHSQlKI0h56U4xI9DOicVjBs9S0yXI3pu3k)

## 주요 기능

# 주요 기능

| 모듈 | 기능 |
|------|------|
| 게이트웨이 | • 서버 라우팅<br>• JWT 인증/인가 |
| 유레카 (Eureka) | • 서비스 디스커버리<br>• 로드 밸런싱 |
| 사용자 모듈 | • 회원가입/로그인<br>• 포스트/댓글 작성<br>• 좋아요<br>• 뉴스피드 제공 |
| 주식 모듈 | • 주식 데이터 제공<br>• 스프링 배치 기반 데이터 관리 |
| 크롤링 모듈 | • 스프링 배치 기반 데이터 크롤링 |

## ERD

![ERD](https://private-user-images.githubusercontent.com/167910692/372817733-e7218aed-5780-4c7f-9f55-08d918f0d15e.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mjc4NjI5MzYsIm5iZiI6MTcyNzg2MjYzNiwicGF0aCI6Ii8xNjc5MTA2OTIvMzcyODE3NzMzLWU3MjE4YWVkLTU3ODAtNGM3Zi05ZjU1LTA4ZDkxOGYwZDE1ZS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMDAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTAwMlQwOTUwMzZaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT01OGZlMTRjMDg2NWVhZWIwMTI4MGI1ZDVlZTliZWJiYmYwNTY2NjhkMjQ2MDI5ZTNmYzg4NzBlOTBiZWUzZTdiJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.s2ldgJBXsA9_n-9Opb2iO62k-qdWJrxzVZjZtKLe58M)


## 트러블슈팅
# MSA 아키텍처 최적화 과정
<details><summary><strong>펼쳐보기</strong></summary>

![아키텍처 변화](https://private-user-images.githubusercontent.com/167910692/372848394-23412a8f-071b-4f3b-9ae7-74b19e5e272d.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mjc4Njk3NjAsIm5iZiI6MTcyNzg2OTQ2MCwicGF0aCI6Ii8xNjc5MTA2OTIvMzcyODQ4Mzk0LTIzNDEyYThmLTA3MWItNGYzYi05YWU3LTc0YjE5ZTVlMjcyZC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMDAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTAwMlQxMTQ0MjBaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT00OTc3ODg4NzUxMmU3N2FjZmNmZjZhZGRlM2RhODE4ZWI2OGE2ZmIwOGQ0Y2ZiMjJmZDUwNGQxZTE1ZTQzZGVlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.KCNRI7vuQWrdciSqFDLYw_IIT0folBHQzqHx1G8WW6I)

## 1. 초기 설계 단계

- Docker와 Docker Compose를 활용한 컨테이너화 구현
    - 각 마이크로서비스를 독립적인 Docker 컨테이너로 패키징
    - Docker Compose를 사용하여 다중 컨테이너 애플리케이션 정의 및 실행

## 2. 문제 인식

- 프로젝트 진행 중 MSA의 핵심 이점인 확장성 제약 문제 인식
    - Docker Compose의 한계: 단일 호스트 환경에서의 운영
    - 동적 스케일링 및 로드 밸런싱의 어려움

## 3. 아키텍처 개선

- Kubernetes 도입 결정 및 구현
    - 자동화된 배포, 스케일링, 관리 기능 활용
    - 선언적 구성 관리를 통한 일관성 있는 환경 구축
    - 롤링 업데이트 및 롤백 기능을 통한 무중단 배포 실현

이러한 최적화 과정을 통해 MSA의 이점을 최대한 활용하고, 시스템의 확장성, 유연성, 그리고 관리 효율성을 크게 향상시켰습니다.

</details>

# Redis 성능 최적화: 사례 연구

<details><summary><strong>펼쳐보기</strong></summary>

## 개요
- **목표**: 주식 관련 데이터를 위한 Redis 서버 운영
- **초기 설정**:
    - MySQL 서버에서 데이터를 전달받아 Redis 서버 초기화
    - MySQL 업데이트 시 Kafka를 통해 데이터 전달 받음

## 발생한 문제
509만 개의 데이터를 Redis로 옮기는 중 메모리 부족으로 서버가 중단되는 현상 발생

### 환경
- Docker Compose를 사용한 로컬 설정
- 제한된 메모리 자원

## 해결 접근 방식

### 고려한 옵션
1. 페이지네이션 처리
2. 스트림 처리

### 선택한 해결책: 스트림 처리
- **이유**: 일반적으로 더 빠른 성능

## 초기 문제점
스트림 처리를 구현했음에도 메모리 문제 지속

### 문제 식별
- VisualVM을 사용한 메모리 프로파일링
- 발견: 엔티티에 사용된 BigDecimal, String 객체들이 가비지 컬렉션되지 않음

![VisualVmCapture](https://private-user-images.githubusercontent.com/167910692/372818786-412767f1-85c5-42c6-94d2-9c8172bd6e1c.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mjc4NjMxMjcsIm5iZiI6MTcyNzg2MjgyNywicGF0aCI6Ii8xNjc5MTA2OTIvMzcyODE4Nzg2LTQxMjc2N2YxLTg1YzUtNDJjNi05NGQyLTljODE3MmJkNmUxYy5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMDAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTAwMlQwOTUzNDdaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1kNTRiZGRkNTA5NzRkZTFmM2JiNzMwODllY2NkMTJhZDBiOWNiMjIzMTdkYTZjOGQzMGQxNDkzNjZjOGNmZjJjJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.tRhVMNSqxEp-l7Sq3Z-fU3Kk1K7I2I2mMtNIH9RJZIU)

### 근본 원인
간과한 점: 엔티티 매니저를 명시하지 않더라도 하나의 트랜잭션은 엔티티 매니저가 관리함. 이로 인해 엔티티의 영속성이 유지되어 메모리 누수 발생

## 해결
- 엔티티 매니저 영속성 분리 구현
- 참조되지 않는 엔티티들이 이제 적절히 가비지 컬렉션됨

## 성능 튜닝: 청크 크기 최적화

### 실험
1. 큰 청크 크기 (20만):
    - 결과: 처리 시간 180,000ms
2. 작은 청크 크기 (1,000):
    - 결과: 처리 시간 150,000ms
3. 중간 청크 크기 (1만):
    - 결과: 처리 시간 130,000ms

### 관찰 결과
- 놀랍게도 작은 청크가 큰 청크보다 더 나은 성능을 보임
- 너무 작은 청크는 잦은 I/O로 인해 성능 저하 발생
- 너무 큰 청크:
    - 에러 발생 시 대처가 더 어려워짐
    - 메모리 사용량 증가로 성능 문제 유발 가능

## 결론
- **최적의 청크 크기**: 10,000
- **근거**:
    - I/O 빈도와 메모리 사용량의 균형을 맞춤
    - 효율적인 에러 처리 가능
    - 전체적인 성능 최적화

## 주요 교훈
1. 트랜잭션에서 JPA와 엔티티 매니저 동작 이해의 중요성
2. 대량 데이터 처리에서 적절한 메모리 관리의 중요성
3. 청크 크기와 같은 처리 매개변수 최적화를 위한 실험적 테스트의 필요성
4. 데이터 처리에서 항상 작은 것이 느리거나 큰 것이 좋은 것은 아님
</details>

# Redis 활용 계획: 주식 데이터 저장 및 성능 최적화
<details><summary><strong>펼쳐보기</strong></summary>

## 목적
- 실제 서비스 배포 시 가장 많은 조회가 예상되는 주식 데이터를 저장
- 응답 속도 개선을 위한 Redis 활용

## 데이터 구조 및 크기 예측

### 단일 레코드 구조
| 필드 | 데이터 타입 | 크기 |
|------|------------|------|
| stock (외래 키) | - | 8 바이트 |
| dateTime | - | 8 바이트 |
| openPrice | BigDecimal | 16 바이트 |
| highPrice | BigDecimal | 16 바이트 |
| lowPrice | BigDecimal | 16 바이트 |
| closePrice | BigDecimal | 16 바이트 |
| volume | Long | 8 바이트 |
| dataType | Enum | 4 바이트 |

**총 레코드 크기:** 92 바이트

### 데이터 양 예측
- **거래일 수:** 10일 (2주 중 주말 제외)
- **일일 거래 시간:** 6.5시간 (390분)
- **분 단위 데이터:** 10 * 390 = 3,900개
- **일일 데이터:** 10개
- **총 레코드 수:** 3,900 + 10 = 3,910개

### 저장 공간 계산
- **단일 종목 필요 공간:** 3,910 * 92 바이트 ≈ 359.72 KB
- **전체 종목 (2,600개) 2주 데이터:** 359.72 KB * 2,600 ≈ 1.8 GB
- **모든 종목 2주 데이터:** 약 940 MB

## 결론
- 940 MB는 상대적으로 큰 용량이 아니라고 판단
- 가장 많이 조회되는 최근 2주일 데이터를 Redis에 저장
- 평균 79.63% 단축

</details>

# 데이터 처리 최적화

<details><summary><strong>펼쳐보기</strong></summary>


## 프로젝트 개요
- 약 2800개 종목 처리 필요
- 현재 10종목만 처리 중 (처리 시간: 1초 미만)

## 인프라 구성
1. 한국투자증권 API 모방 서버
  - 실시간 데이터 제공
  - 요청 제한 없음
2. 데이터 처리/정제 서버
3. 데이터 관리 서버

## 현재 방식
- Spring Batch, 리더 선출을 통한 중앙 집중식 데이터 처리
- Kafka를 통한 데이터 전송

## 개선 방식
1. 데이터 처리 최적화
  - Spring Batch와 Kubernetes 기반 샤딩으로 분산 처리 구현
2. 데이터 스트리밍 최적화
  - Kafka 파티셔닝을 통한 병렬 스트리밍 구현

## 성능 개선 결과

| 프로세스         | 기존 시간 | 개선 시간 | 개선율 |
|------------------|-----------|-----------|--------|
| 데이터 처리      | 11.3 s    | 4.3 s     | 62%    |
| 데이터 전송      | 700 ms    | 230 ms    | 67%    |
| 데이터 저장      | 1100 ms   | 330 ms    | 70%    |
</details>