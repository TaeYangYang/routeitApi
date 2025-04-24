# RouteIt 프로젝트 API 서버

---

[개요](#개요)\
[개발 환경](#개발-환경)

---
### 개요
여행지의 경로를 공유하자!

기존 RouteIt 프로젝트 구조 변경 및 분리해서 생성한 프로젝트

---

### 개발 환경
- DB
    - MariaDB
    - Redis
    - MongoDB
- Server
    - Kotlin 1.9.25(JDK 17)
    - Spring Boot 3.4.4
    - Spring Security 6.4.2
    - Gradle 8.11.1
    - JPA
    - JWT
    - JUnit
- 인프라 및 배포
    - AWS EC2, RDS
    - Docker
- 문서
    - Swagger
---

port : 8081

---

### 실행방법
### 로컬
프로젝트 루트 경로에서 아래 명령어 실행
> docker compose -f docker-compose.yml -f docker-compose-local.yml up --build -d


