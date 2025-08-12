# Hanaro Shopping Mall Project

하나로(Hanaro) 쇼핑몰 백엔드 API 프로젝트입니다. Spring Boot와 JPA를 기반으로 사용자, 상품, 주문, 인증 등 쇼핑몰의 핵심 기능을 구현했습니다.

## 📚 기술 스택

- **언어**: Java 17
- **프레임워크**: Spring Boot 3.x
- **데이터베이스**: MySQL, JPA (Hibernate)
- **인증**: Spring Security, JWT (JSON Web Token)
- **API 문서**: Springdoc (Swagger UI)
- **빌드 도구**: Gradle
- **기타**: Lombok, Querydsl, Spring Batch

## 🚀 실행 방법

1.  **데이터베이스 설정**:
    - MySQL 서버에 `hanarodb` 스키마를 생성합니다.
    - `src/main/resources/application.properties` 파일의 `spring.datasource` 관련 설정을 자신의 로컬 DB 환경에 맞게 수정합니다. (기본 계정: `hanaro` / `12345678`)

2.  **애플리케이션 실행**:
    - 프로젝트를 IDE로 열고 `HanaroApplication.java` 파일을 실행합니다.
    - 또는, 터미널에서 프로젝트 루트 디렉토리로 이동 후 아래 명령어를 실행합니다.
      ```bash
      ./gradlew bootRun
      ```

3.  **애플리케이션 확인**:
    - 서버가 정상적으로 실행되면, `http://localhost:8080` 주소로 API 요청을 보낼 수 있습니다.
    - 서버가 시작될 때 `data.sql`에 의해 기본 관리자/사용자 계정과 샘플 상품 데이터가 자동으로 생성됩니다.

## 📝 API 명세

전체 API 명세는 애플리케이션 실행 후 아래 Swagger UI 주소에서 확인하고 직접 테스트해볼 수 있습니다.

- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Swagger UI 우측 상단의 드롭다운 메뉴를 통해 **Public, User, Admin API 그룹**을 선택하여 볼 수 있습니다.

### 🏛️ Public API (인증 불필요)

| 기능 | HTTP Method | URL | 설명 |
| --- | --- | --- | --- |
| **회원 가입** | `POST` | `/api/users/signup` | 새로운 사용자를 등록합니다. |
| **로그인** | `POST` | `/api/users/login` | 로그인 성공 시 Access/Refresh Token을 발급합니다. |
| **토큰 재발급** | `POST` | `/api/users/reissue` | Refresh Token으로 새로운 Access Token을 재발급합니다. |
| **전체 상품 조회** | `GET` | `/api/products` | 판매 중인 모든 상품 목록을 페이징하여 조회합니다. |
| **상품 상세 조회** | `GET` | `/api/products/{productId}` | 특정 상품의 상세 정보를 조회합니다. |
| **상품 검색** | `GET` | `/api/products/search` | 키워드로 상품을 검색하고 페이징하여 조회합니다. |

### 👤 User API (사용자 인증 필요)

| 기능 | HTTP Method | URL | 설명 |
| --- | --- | --- | --- |
| **장바구니 상품 추가** | `POST` | `/api/user/cart/products` | 장바구니에 상품을 추가합니다. |
| **내 장바구니 조회** | `GET` | `/api/user/cart` | 현재 사용자의 장바구니 정보를 조회합니다. |
| **장바구니 상품 수량 변경**| `PATCH` | `/api/user/cart/products/{cartProductId}` | 장바구니 상품의 수량을 변경합니다. |
| **장바구니 상품 삭제** | `DELETE` | `/api/user/cart/products/{cartProductId}` | 장바구니에서 특정 상품을 삭제합니다. |
| **주문 생성** | `POST` | `/api/user/orders/from-cart` | 장바구니의 모든 상품으로 새로운 주문을 생성합니다. |
| **내 주문 목록 조회** | `GET` | `/api/user/orders` | 현재 사용자의 주문 목록을 페이징하여 조회합니다. |
| **주문 상세 조회** | `GET` | `/api/user/orders/{orderId}` | 특정 주문의 상세 내역을 조회합니다. |
| **주문 취소** | `POST` | `/api/user/orders/{orderId}/cancel` | 특정 주문을 취소합니다. (배송 시작 전까지만 가능) |

### 👮 Admin API (관리자 인증 필요)

| 기능 | HTTP Method | URL | 설명 |
| --- | --- | --- | --- |
| **전체 회원 목록 조회** | `GET` | `/api/admin/users` | 모든 사용자 목록을 조회합니다. |
| **회원 삭제** | `DELETE` | `/api/admin/users/{userId}` | 특정 사용자를 삭제합니다. |
| **상품 등록** | `POST` | `/api/admin/products` | `multipart/form-data` 형식으로 새 상품을 등록합니다. |
| **상품 정보 수정** | `PUT` | `/api/admin/products/{productId}` | 특정 상품의 전체 정보를 수정합니다. |
| **상품 재고 수정** | `PATCH` | `/api/admin/products/{productId}/stock` | 특정 상품의 재고만 수정합니다. |
| **상품 삭제** | `DELETE` | `/api/admin/products/{productId}` | 특정 상품을 삭제합니다. |
| **주문 내역 검색** | `GET` | `/api/admin/orders` | 조건(사용자명, 주문상태)으로 주문을 검색하고 페이징합니다. |
| **매출 통계 조회** | `GET` | `/api/admin/sales` | 지정된 기간(startDate, endDate)의 일별 매출을 조회합니다. |

## ✨ 주요 기능 확인 방법

### 배치 작업 (매출 통계)

- **실행 시점**: 매일 00시 00분에 자동으로 실행됩니다.
- **결과 확인**:
    - `logs/business_order.log` 파일에 당일 처리된 주문 로그가 기록됩니다.
    - `daily_sales` 데이터베이스 테이블에 전날의 총 매출액이 기록됩니다.
    - 관리자 API(`GET /api/admin/sales`)를 통해 조회할 수 있습니다.

### 스케줄러 (주문 상태 자동 변경)

- **실행 주기**: 1분마다 실행됩니다.
- **기능**: 주문의 상태 변경 시간(`status_update_time`)을 기준으로 아래 조건에 따라 주문 상태(`status`)를 자동으로 변경합니다.
    - **결제완료** → (5분 후) → **배송준비중**
    - **배송준비중** → (15분 후) → **배송중**
    - **배송중** → (1시간 후) → **배송완료**
- **결과 확인**: `logs/app.log` 파일과 `orders` 데이터베이스 테이블의 `status` 필드 값 변경을 통해 확인할 수 있습니다.

### Actuator (애플리케이션 모니터링)

- 아래 엔드포인트를 통해 애플리케이션의 상태를 확인할 수 있습니다.
- **Health Check**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Metrics**: [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics)
- **Beans**: [http://localhost:8080/actuator/beans](http://localhost:8080/actuator/beans)
- **Env**: [http://localhost:8080/actuator/env](http://localhost:8080/actuator/env)
