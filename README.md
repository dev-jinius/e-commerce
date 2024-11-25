# e-commerce
이 프로젝트는 TDD(테스트 주도 개발)와 클린 아키텍처를 기반으로 구축한 이커머스 서버 애플리케이션입니다.
객체지향 설계 원칙(OCP, DIP)을 준수하며, 레이어드 아키텍처를 적용해 확장성과 유지보수성을 극대화했습니다.

추후 MSA(Microservices Architecture)로 확장 가능하도록 도메인 분리를 고려해 설계했습니다.

## 주요 특징
- TDD 적용 : 모든 비즈니스 로직은 테스트 주도 개발 방식을 통해 설계 및 구현
- Layered + Clean Architecture : 도메인 중심 설계로 애플리케이션 핵심 로직을 보호하고 외부와 분리
- OCP, DIP 준수
- MSA 전환 고려한 설계
  - 도메인 중심 설계와 DTO(Data Transfer Object)를 사용하여 서비스 간 독립성 확보
  - 이벤트 기반 통신 구조를 도입해 비동기 메시징 방식으로 확장 가능
  - 개별 도메인의 상태 및 데이터 관리가 독립적으로 수행될 수 있도록 설계
- 데이터 무결성 보장: 재고 관리와 주문 처리의 동시성 문제를 고려하여 설계

--- 
## Progress
- ### [기술 스택](#tech-stack)
- ### [요구 사항](#requirements)
- ### [시나리오 설계](#1-시나리오-설계)
    + #### [주요 기능](#1-1-주요-기능)
    + #### [시퀀스 다이어그램](#1-2-시퀀스-다이어그램-작성)
    + #### [ERD 설계](#1-3-erd-설계)
- ### [API Specsification](#2-api-specification)
    + #### [Swagger](#2-1-swagger-접속)
- ### [Deep Dive](#3-deep-dive)
---


## Tech Stack
- Backend: Java, Spring Boot
- Database: MariaDB
- Cache & Concurrency Control: Redis, Redisson, DB Lock(Optimistic Lock)
- Build Tool: Gradle
- Testing: JUnit5, AssertJ
- Containerization: Docker
- Version Control: Git

---

## Requirements
- 아래 4가지 API 를 구현한다.
    - 잔액 충전 / 조회 API
    - 상품 조회 API
    - 주문 / 결제 API
    - 인기 판매 상품 조회 API
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 한다.
- 동시성 이슈를 고려하여 구현한다.


---


## 1. 시나리오 설계
### 1-1 주요 기능

1️⃣ 잔액 충전 / 조회 API

- 결제에 사용될 금액을 충전하는 API 를 작성한다.
- 사용자 식별자 및 충전할 금액을 받아 잔액을 충전한다.
- 사용자 식별자를 통해 해당 사용자의 잔액을 조회한다.

2️⃣ 상품 조회 API

- 상품 정보 ( ID, 이름, 가격, 잔여수량 ) 을 조회하는 API 를 작성한다.

3️⃣ 주문 / 결제 API

- 사용자 식별자와 (상품 ID, 수량) 목록을 입력받아 주문하고 결제를 수행하는 API 를 작성한다.
- 결제는 기 충전된 잔액을 기반으로 수행하며 성공할 시 잔액을 차감한다.
- 데이터 분석을 위해 결제 성공 시에 실시간으로 주문 정보를 외부 어플리케이션에 전송한다.

4️⃣ 상위 상품 조회 API
- 최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 제공하는 API

```
  💡 KEY POINT
```
- 동시에 여러 주문이 들어올 경우, 유저의 보유 잔고에 대한 처리가 정확해야 한다.
- 각 상품의 재고 관리가 정상적으로 이루어져 잘못된 주문이 발생하지 않도록 해야 한다.

### 1-2 시퀀스 다이어그램 작성
*  잔액 충전 / 조회 API</br>
```mermaid
---
title: 포인트 조회 시퀀스 다이어그램
---
sequenceDiagram
    actor c as Client
    participant app as Application
    participant domain as Domain
    participant db as DB
    
    c->>+app: 포인트 조회 요청(GET /point)
    app->>+domain: 요청 전달
    domain->>+db: 유저 포인트 조회
    alt 유저가 존재하지 않는 경우
        db-->>domain: 빈 값 반환 (유저 없음)
    else 유저가 존재하는 경우
        db-->>-domain: 유저 포인트 정보 반환
    end
    domain-->>-app: 반환 결과 전달 (유저 포인트)
    app-->>-c: 포인트 조회 응답(JSON)
```
```mermaid
---
title: 포인트 충전 시퀀스 다이어그램
---
sequenceDiagram
    actor c as Client
    participant app as Application
    participant domain as Domain
    participant db as DB
    
    c->>+app: 포인트 충전 요청(PATCH /charge)
    app->>+domain: 요청 전달
    domain->>+db: 유저 포인트 조회
    alt 유저가 존재하지 않는 경우
        db-->>domain: 빈 값 반환 (유저 없음)
    else 유저가 존재하는 경우
        db-->>-domain: 유저 포인트 정보 반환
        domain->>domain: 포인트 충전
        domain->>+db: 유저 포인트 업데이트
        db-->>-domain: 유저 포인트 정보 반환
    end
    domain-->>-app: 반환 결과 전달 (유저 포인트)
    app-->>-c: 포인트 조회 응답(JSON)
```

* 상품 조회 API</br>
```mermaid
---
title: 전체 상품 조회 시퀀스 다이어그램
---
sequenceDiagram
    actor c as Client
    participant app as Application
    participant domain as Domain
    participant db as DB
    
    c->>+app: 상품 목록 조회 요청(GET /productList)
    app->>+domain: 요청 전달
    domain->>+db: 상품 목록 조회
    alt 상품 개수 > 0
        db-->>domain: 빈 배열 [] 반환
    else 상품 개수 <= 0
        db-->>-domain: 상품 목록 반환
    end
    domain-->>-app: 반환 결과 전달(상품 목록)
    app-->>-c: 상품 목록 조회 응답(JSON)
```
* 주문 / 결제 API</br>
```mermaid
---
title: 상품 주문 시퀀스 다이어그램
---
sequenceDiagram
    actor c as Client
    participant app as Application
    participant order as Order
    participant item as OrderItem
    participant pay as Payment
    participant user as User
    participant stock as Stock
    participant db as DB
    participant api as External API
    
    c->>+app: 주문 요청 (POST /order)
    app->>+order: 주문서 생성 (주문 ID, 유저 ID)
    order->>+item: 주문 항목 추가 요청 (상품 리스트)
    item-->>-order: 상품 리스트 반환
    order->>order: 주문 상태 'PENDING'으로 업데이트 
    order->>+db: 주문 상태 업데이트(PENDING)
    db-->>-order: 주문 정보(주문서) 반환
    
    app->>+pay: 포인트 결제 요청 (유저ID, 주문 금액)
    pay->>+user: 유저 포인트 조회 (유저ID)
    user->>+db: 유저 포인트 조회
    db-->>-user: 유저 포인트 정보 반환
    
    alt 잔액 포인트 >= 주문 금액
        user->>user: 포인트 차감
        user->>+db: 유저 포인트 업데이트
        db-->>-user: 유저 포인트 반환
        user-->>pay: 포인트 차감 성공
        pay-->>app: 포인트 결제 성공
        app->>order: 주문 상태 업데이트 요청
        order->>order: 주문 상태 'PAID'로 업데이트
        order->>+db: 주문 상태 업데이트(PAID)
        db-->>order: 주문 정보(주문서) 반환
        
        app->>+stock: 재고 조회 요청
        stock->>+db: 재고 조회
        db-->>-stock: 재고 정보 반환
        
        alt 재고 수량 >= 주문 상품 수량
            stock->>stock: 재고 차감
            stock-->>app: 재고 차감 성공
            app->>order: 주문 상태 업데이트 요청
            order->>order: 주문 상태 'COMPLETE'로 업데이트
            order->>+db: 주문 상태 업데이트(COMPLETE)
            db-->>-order: 주문 정보(주문서) 반환
            order-->>app: 주문 성공
            app->>+api: 주문 정보 전송
            app-->>c: 주문 성공 응답
            api-->>-app: (비동기) 처리 중..
        else 재고 수량 < 주문 상품 수량
            stock-->>-app: 재고 차감 실패
            app->>order: 주문 상태 업데이트 요청
            order->>order: 주문 상태 'CANCELED'로 업데이트 요청
            order->>+db: 주문 상태 업데이트(CANCELED)
            db-->>-order: 주문 정보(주문서) 반환
            order-->>app: 주문 실패
            app-->>c: 주문 실패 응답 (재고 부족)
        end
        
    else 잔액 포인트 < 주문 금액
        user-->>pay: 포인트 부족 예외
        pay-->>-app: 포인트 결제 실패
        app->>order: 주문 상태 업데이트 요청
        order->>order: 주문 상태 'CANCELED'로 업데이트 요청
        order->>+db: 주문 상태 업데이트(CANCELED)
        db-->>-order: 주문 정보(주문서) 반환
        order-->>-app: 주문 실패
        app-->>-c: 주문 실패 응답 (포인트 부족)
    end
```

* 상위 상품 조회 API</br>

* 장바구니에 상품 조회/추가/삭제 API (심화)


### 1-3 ERD 설계
* ERD 작성
  ![이커머스](https://github.com/user-attachments/assets/38f6bd8b-6191-413e-b9bf-d5b4c85324e0)


## 2. API specification
### 2-1 Swagger 접속
 - http://localhost:8080/swagger-ui/index.html

## 3. Deep Dive

## 4. Manual
1. application.yml에서 데이터베이스 설정 수정. 
2. Docker, docker-compose를 사용해 애플리케이션을 컨테이너로 실행
   - /docs/docker 위치에서 docker-compose up --build
3. DB 실행 및 연결 확인
   - host : localhost
   - port : 13306
4. API 테스트
   - http://localhost:8080/swagger-ui/index.html
