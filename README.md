# 종목 토론방 📊

종목토론방 API는 주식 투자자들이 실시간으로 종목에 대해 토론하고 정보를 공유할 수 있는 플랫폼을 제공합니다.

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://www.java.com/)
[![Spring](https://img.shields.io/badge/Spring-3.3.2-green?style=flat-square&logo=spring)](https://spring.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0.33-blue?style=flat-square&logo=mysql)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-7.0.8-red?style=flat-square&logo=redis)](https://redis.io/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-Latest-326CE5?style=flat-square&logo=kubernetes)](https://kubernetes.io/)

## 목차
- [기술 스택](#기술-스택)
- [아키텍처](#아키텍처)
- [주요 기능](#주요-기능)
- [ERD](#erd)
- [트러블슈팅](#트러블슈팅)
  - [MSA 아키텍처 최적화](#msa-아키텍처-최적화)
  - [주식 데이터 관리](#주식-데이터-관리)
  - [데이터 처리 최적화](#데이터-처리-최적화)
  - [데이터베이스 조회 성능 개선](#데이터베이스-조회-성능-개선)

## 기술 스택

### Backend
- Java 21
- Spring Framework 3.3.2
  - Spring Data JPA
  - Spring Batch
  - Spring Data Redis
  - Spring Security
  - Spring WebFlux
- JJWT 0.12.3
- Swagger v3 2.2.9

### Database
- MySQL 8.0.33
- Redis 7.0.8
- AWS S3

### DevOps
- Docker
- Kubernetes

## 아키텍처

![Architecture Diagram](https://github.com/user-attachments/assets/e7db877d-57c1-4823-a768-78090b62bc22)

## 주요 기능

| 모듈         | 기능                                              |
|------------|-------------------------------------------------|
| 🔍 Envoy   | • 디스커버리<br>• 로드밸런싱<br>• 라우팅                         |
| 🚪 인증 모듈   | • JWT 인증/인가                                     |
| 👥 사용자 모듈  | • 회원가입/로그인<br>• 포스트/댓글 작성<br>• 좋아요<br>• 뉴스피드 제공 |
| 📈 주식 모듈   | • 주식 데이터 제공<br>• 스프링 배치 기반 데이터 관리               |
| 🕷️ 크롤링 모듈 | • 스프링 배치 기반 데이터 크롤링                             |

## ERD

![ERD Diagram](https://github.com/user-attachments/assets/2cad6d01-710b-4e3c-9261-2d03cca24a78)

## 트러블슈팅

### MSA 아키텍처 최적화

- 초기: Docker와 Docker Compose 활용한 컨테이너화
- 문제 인식: MSA의 확장성 제약
- 해결: **Kubernetes** 도입으로 아키텍처 개선

![Kubernetes Diagram](https://github.com/user-attachments/assets/23412a8f-071b-4f3b-9ae7-74b19e5e272d)

### 주식 데이터 관리

- 제한사항: API 호출 제한 (한국투자증권 API: 초당 2회, 일 5000회)
- 해결:
  - 네이버 API 활용으로 초기화 시간 단축 (17분 → 2초/종목)
  - 10개 핵심 종목만 실시간 관리 (일 3910회 호출)
- 제공 데이터: 5년 일일 데이터, 1주일 분단위 데이터

### 데이터 처리 최적화

- 규모: 약 2800개 종목 (현재 10종목 처리)
- 구성: 테스트를 위한 API호출 제한이 없는 모방 서버, 데이터 처리/정제 서버, 데이터 관리 서버
- 개선 전: Spring Batch 중앙 집중식 처리, Kafka 전송
- 개선 후: Spring Batch + Kubernetes 샤딩, Kafka 파티셔닝
- 성능 향상:
  - 데이터 처리: 11.3s → 4.3s (✅ 62% 개선)
  - 데이터 전송: 700ms → 230ms (✅ 67% 개선)
  - 데이터 저장: 1100ms → 330ms (✅ 70% 개선)

### 데이터베이스 조회 성능 개선

| 단계 | 방법 | 성능 개선 |
|------|------|-----------|
| 1️⃣ | 엔티티 ID: Auto Increment → 복합키 | 335ms → 48ms (✅ 85% 개선) |
| 2️⃣ | JPA → 네이티브 쿼리 | 48ms → 35ms (✅ 27% 추가 개선) |
| 3️⃣ | Redis 캐시 도입 | 35ms → 10ms (✅ 71% 추가 개선) |
