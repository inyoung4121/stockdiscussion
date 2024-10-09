# 종목 토론방

- 종목토론방 API는 주식 투자자들이 실시간으로 종목에 대해 토론하고 정보를 공유할 수 있는 플랫폼을 제공합니다.




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


# 트러블슈팅

## MSA 아키텍처 최적화 과정
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

