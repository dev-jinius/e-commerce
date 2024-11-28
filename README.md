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


### 1-3 ERD 설계
* ERD 작성
  ![이커머스](https://github.com/user-attachments/assets/38f6bd8b-6191-413e-b9bf-d5b4c85324e0)


## 2. API specification
### 2-1 Swagger 접속
 - http://localhost:8080/swagger-ui/index.html

## 3. Deep Dive
### 3-1. Concurrency Control
#### Case 1. 동일 유저에 대한 포인트 충전(또는 사용) 요청이 동시에 들어온 경우
- 문제 : 유저가 포인트 충전 시, 의도치 않은 충전 또는 사용 요청을 동시에 보낼 가능성이 있어 갱신 손실 문제가 생길 수 있습니다.
  ```
  1. tx1, tx2 동시에 두 트랜잭션이 시작하여 유저 1의 잔액 포인트(10000)를 조회
  2. tx1 트랜잭션이 50000 포인트 충전해 유저 1의 잔액 포인트를 60000 으로 업데이트
  3. tx2 트랜잭션은 100000 포인트 충전해 유저 1의 잔액 포인트를 110000 으로 업데이트
  4. tx1 트랜잭션 COMMIT -> 유저 1의 잔액 포인트는 60000 으로 갱신
  5. tx2 트랜잭션 COMMIT -> 유저 1의 잔액 포인트를 110000 으로 갱신 (tx1의 갱신한 내용을 무시하고 갱신 손실 발생)
  ```
- 해결 : 유저 실수로 중복 요청을 보낸 경우, 선점된 1개의 요청만 성공하도록 설계
- 낙관적 락 사용
    - 이유: 동시성 문제(충돌)가 발생할 확률이 낮으며, 충돌로 인한 비즈니스 로직의 실패처리로 이어져도 괜찮기 때문입니다.
    - 선점된 1개의 요청만 성공하며, 그 외 요청에는 실패 처리를 함으로써 데이터 무결성을 보장한다.
    - 충돌 발생 시 OptimisticLockingFailureException 예외를 발생시켜 중복 요청으로 실패 처리를 한다.

#### Case 2. 다수의 유저가 동일 상품을 동시에 주문 요청하는 경우 
- 문제 : 서로 다른 유저가 동시에 동일 상품을 주문해 재고 차감 처리 시, 갱신 손실 문제가 생길 수 있습니다.
- 사용한 데이터베이스는 MariaDB 10.6 버전으로 트랜잭션 격리 수준은 ‘REAPEATABLE-READ’입니다.
    - 읽은 데이터의 일관성을 유지하지만, 동일 데이터 수정 작업에 대한 동시성 보장은 하지 못합니다.
  
  ```
  1. tx1, tx2 동시에 두 트랜잭션이 시작하여 유저 1의 잔액 포인트(10000)를 조회
  2. tx1 트랜잭션이 50000 포인트 충전해 유저 1의 잔액 포인트를 60000 으로 업데이트
  3. tx2 트랜잭션은 100000 포인트 충전해 유저 1의 잔액 포인트를 110000 으로 업데이트
  4. tx1 트랜잭션 COMMIT -> 유저 1의 잔액 포인트는 60000 으로 갱신
  5. tx2 트랜잭션 COMMIT -> 유저 1의 잔액 포인트를 110000 으로 갱신 (tx1의 갱신한 내용을 무시하고 갱신 손실 발생)
  ```
- 해결 : Pub/Sub 방식을 이용해 락을 획득, 재고 처리 후 락이 해제되면 subscribe 하는 다른 트랜잭션이 락 해제 신호를 받고 락을 획득하는 패턴 설계
- 분산 락 Redisson 사용
  - 상품 주문 시 충돌 횟수가 빈번할 수 있으며 비즈니스 로직이 무조건 성공해야 하기 때문에 낙관적 락은 제외했다.
  - 비관적 락을 고려헀을 때, DB가 분산된 환경에서 데이터 정합성이 보장되지 않아 제한점이 있다.
  - 추후 DB 샤딩을 통해 분산 환경 시스템이 되었을 때를 대비하여 분산락을 사용해보기로 생각했습니다.


## 4. Manual
1. application.yml에서 데이터베이스 설정 수정. 
2. Docker, docker-compose를 사용해 애플리케이션을 컨테이너로 실행
   - /docs/docker 위치에서 docker-compose up --build
3. DB 실행 및 연결 확인
   - host : localhost
   - port : 13306
4. API 테스트
   - http://localhost:8080/swagger-ui/index.html
