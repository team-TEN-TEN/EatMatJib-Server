# 🍽️ 위치 기반 맛집 추천 서비스 `이맛집(EatMatJib)`

## 목차

- [개요](#개요)
- [개발 환경](#skills)
- [Running Tests](#running-tests)
- [API Reference](#api-reference)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)
- [구현과정(설계 및 의도)](#구현과정설계-및-의도)
- [Authors](#authors)

## 개요

`이맛집(EatMatJib)`은 Open
API인 [서울시 일반음식점 인허가 정보](https://data.seoul.go.kr/dataList/OA-16094/S/1/datasetView.do) 데이터를 활용하여, 지역
음식점 목록을 자동으로 업데이트하고 활용합니다.

`중부원점TM(EPSG:2097)` 좌표계 기준으로 사용자 위치 기반 맛집을 추천하여 더 나은 다양한 음식 경험을 제공하고, 음식을 좋아하는 사람들 간의 소통과 공유를 촉진하는
서비스입니다.

## 개발 환경

<img src="https://img.shields.io/badge/Language-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"><img src="https://img.shields.io/badge/17-515151?style=for-the-badge">

<img src="https://img.shields.io/badge/Framework-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/3.3.2-515151?style=for-the-badge"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">

<img src="https://img.shields.io/badge/Database-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=&logoColor=white"> <img src="https://img.shields.io/badge/querydsl-6DB33F?style=for-the-badge&logo=&logoColor=white">

<img src="https://img.shields.io/badge/Build-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"><img src="https://img.shields.io/badge/8.8-515151?style=for-the-badge">

<img src="https://img.shields.io/badge/Deployment-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/aws%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white"> <img src="https://img.shields.io/badge/flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white"> <img src="https://img.shields.io/badge/aws rds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">

<img src="https://img.shields.io/badge/version control-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

## Running Tests

JaCoCo를 활용해 코드 커버리지를 측정했습니다.

테스트를 실행하려면 다음 명령어를 입력합니다.

```bash
./gradlew test
```

> `build/jacoco/index.html/index.html`에 커버리지 보고서가 생성됩니다.

## API Reference

[API 명세서 (Swagger)]()

<details>
<summary>사용자 회원가입 API</summary>

> 사용자는 계정, 비밀번호로 회원가입이 가능합니다.

```java
POST /api/v1/members/register
```

#### Request

```json
{
  "account": "tenten",
  "password": "password12!"
}
```

| Field      | Type     | Description |
|------------|----------|-------------|
| `account`  | `String` | 계정          |
| `password` | `String` | 비밀번호        |

#### Response

**1. 201 Created**

```json
HTTP/1.1 201
Content-Type: application/json

{
  "memberId": "1"
}
```

| Field      | Type   | Description |
|------------|--------|-------------|
| `memberId` | `Long` | 사용자 id      |

**2. 400 Bad Request**

```json
HTTP/1.1 400
Content-Type: application/json

{
  "message": "잘못된 요청입니다. 입력값을 확인하고 다시 시도해주세요.",
  "detail": [
    "계정은 1~50자만 가능합니다.",
    "계정은 필수 입력입니다.",
    "비밀번호는 필수 입력입니다.",
    "비밀번호는 1) 최소 10자 이상, 2) 숫자/문자/특수문자(!@#$%^&*) 중 2가지 이상 포함, 3) 3회 이상 연속되는 문자를 사용할 수 없습니다."
  ]
}
```

**3. 409 Conflict**

```json
HTTP/1.1 409
Content-Type: application/json

{
  "message": "이미 사용중인 계정입니다."
}
```

</details>

<details>
<summary>사용자 로그인 API</summary>

> 사용자는 계정, 비밀번호로 로그인이 가능하고, 로그인이 성공하면 JWT가 발급됩니다.

```java
POST /api/v1/members/login
```

#### Request

```json
{
  "account": "tenten",
  "password": "password12!"
}
```

| Field      | Type     | Description |
|------------|----------|-------------|
| `account`  | `String` | 계정          |
| `password` | `String` | 비밀번호        |

#### Response

**1. 200 Ok**

```json
HTTP/1.1 200
Content-Type: application/json

{
  "account": "tenten",
  "accessToken": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZW50ZW4yIiwicm9sZSI6IlBSRV9NRU1CRVIiLCJhY2NvdW50IjoidGVudGVuMiIsImlhdCI6MTcyNDU5NTI3NiwiZXhwIjoxNzI4MTk1Mjc2fQ.kHK0gWjmKkJSjJCWCnoSmP3pGnT5O9OWOf74iQ-yupl7TzenIEXJvzu00UT0dxYq",
  "refreshToken": "eyJhbGciOiJIUzM4NCJ9.eyJleHAiOjI5MzQxOTUyNzZ9.mzsurji239LQi8mVYlW_f6Flld9zt36Sh5X9J2RamlymONrRjek13inUabyB4KO8"
}
```

| Field          | Type     | Description |
|----------------|----------|-------------|
| `account`      | `String` | 계정          |
| `accessToken`  | `String` | JWT 액세스 토큰  |
| `refreshToken` | `String` | JWT 리프레시 토큰 |

**2. 400 Bad Request**

```json
HTTP/1.1 400
Content-Type: application/json

{
  "message": "잘못된 요청입니다. 입력값을 확인하고 다시 시도해주세요.",
  "detail": [
    "계정은 필수 입력입니다.",
    "비밀번호는 필수 입력입니다."
  ]
}
```

**3. 401 Unauthorized**

```json
HTTP/1.1 401
Content-Type: application/json

{
  "message": "존재하지 않는 계정입니다."
}
```

```json
HTTP/1.1 401
Content-Type: application/json

{
  "message": "비밀번호를 잘못 입력했습니다."
}
```

**4. 403 Forbidden**

```json
HTTP/1.1 403
Content-Type: application/json

{
  "message": "서비스 회원이 아닙니다. 이메일 인증을 먼저 해주세요."
}
```

</details>

<details>
<summary>사용자 정보 API</summary>

> 비밀번호를 제외한 모든 사용자 정보를 조회합니다.

```java
GET /api/v1/members/info
```

#### Response

**1. 200 Ok**

```json
HTTP/1.1 200
Content-Type: application/json

{
  "id": "1",
  "account": "tenten",
  "x": "200000.123456789",
  "y": "200000.123456789",
  "isRecommendationActive": "true",
  "joindAt": "2024-09-02"
}
```

| Field                    | Type         | Description    |
|--------------------------|--------------|----------------|
| `id`                     | `Long`       | 사용자 id         |
| `account`                | `String`     | 계정             |
| `x`                      | `BigDecimal` | x 좌표           |
| `y`                      | `BigDecimal` | y 좌표           |
| `isRecommendationActive` | `Boolean`    | 점심 추천 기능 사용 여부 |
| `joindAt`                | `LocalDate`  | 가입 날짜          |

**2. 404 Not Found**

```json
HTTP/1.1 404
Content-Type: application/json

{
  "message": "존재하지 않는 멤버입니다."
}
```

</details>

<details>
<summary>사용자 설정 업데이트 API</summary>

> 사용자의 위치(x, y 좌표) 및 점심 추천 기능 사용 여부를 업데이트합니다.

```java
PATCH /api/v1/members/info
```

#### Request

```json
{
  "lat": "37.5665",
  "lon": "126.9780",
  "isRecommendationActive": true
}
```

| Field                    | Type      | Description    |
|--------------------------|-----------|----------------|
| `lat`                    | `Double`  | 위도             |
| `lon`                    | `Double`  | 경도             |
| `isRecommendationActive` | `Boolean` | 점심 추천 기능 사용 여부 |

#### Response

**1. 200 Ok**

```json
HTTP/1.1 200
Content-Type: application/json

{
  "x": "200000.123456789",
  "y": "200000.123456789",
  "isRecommendationActive": true,
}
```

| Field                    | Type         | Description    |
|--------------------------|--------------|----------------|
| `x`                      | `BigDecimal` | x 좌표           |
| `y`                      | `BigDecimal` | y 좌표           |
| `isRecommendationActive` | `Boolean`    | 점심 추천 기능 사용 여부 |

**2. 404 Not Found**

```json
HTTP/1.1 404
Content-Type: application/json

{
  "message": "존재하지 않는 멤버입니다."
}
```

</details>

<details>
<summary>맛집 상세 API</summary>

> 사용자가 음식점의 상세 정보를 조회합니다.

```
GET /api/v1/restaurants/{restaurandId}/detail
```

#### response

**1. 200 OK**
```json
HTTP/1.1 200
Content-Type: application/json
[
  {
  "id": 1,
  "name": "김밥천국",
  "zipCode": "12345",
  "address": "서울시 강남구",
  "cuisine": "양식",
  "x": 0E-10,
  "y": 0E-10,
  "phoneNumber": "010-2",
  "homepageUrl": "http://",
  "avgScore": 0.00,
  "viewCount": 10,
  "updatedAt": "2023-01-01T14:00:00",
  "reviews": [
  {
  "id": 2,
  "content": "짱",
  "score": 5,
  "createdAt": "2024-09-02T14:00:00"
  }
]
```

**2. 404 Not Found**
```json
HTTP/1.1 404
Content-Type: application/json

{
  "message" : "음식점이 존재하지 않습니다."
}
```
</details>

<details>
<summary>맛집 목록 API</summary>

> 사용자 위도, 경도 위치에서 범위(1, 5, 10km) 이내의 맛집 목록을 조회합니다.

```java
GET /api/v1/restaurants
```

#### Request

| Parameter    | Type     | Default Value | Description |
|:-------------|:---------|:--------------|:------------|
| `lat`        | `double` | 필수 값          | 위도          |
| `lon`        | `double` | 필수 값          | 경도          |
| `range`      | `int`    | 필수 값          | 범위(km)      |
| `keyword`    | `String` | 선택 값          | 검색어         |
| `filterBy`   | `String` | 선택 값          | 필터링 기준      |
| `orderBy`    | `String` | distance,rate | 정렬 기준       |
| `pageNumber` | `int`    | 0             | 페이지 번호      |
| `pageSize`   | `int`    | 10            | 페이지 크기      |

#### Response

**1. 200 Ok**

```json
HTTP/1.1 200
Content-Type: application/json

[
{
  "id": "1",
  "name": "치카바",
  "address": "서울특별시 송파구 송이로20길 12-1, 1층 101호 (가락동)",
  "zipCode": "05712",
  "cuisine": "일식",
  "phoneNumber": "02 448 6648",
  "homepageUrl": "https://chikaba.com",
  "avgScore": "4.5"
},
...
{
  "id": "2",
  "name": "매취랑 동대문점",
  "address": "서울특별시 중구 을지로6가 18-137",
  "zipCode": "05713",
  "cuisine": "한식",
  "phoneNumber": "02 448 6649",
  "homepageUrl": "https://dongdaemun.com",
  "avgScore": "4.3"
}
]
```

| Parameter     | Type         | Description |
|:--------------|:-------------|:------------|
| `id`          | `Long`       | 사업장명 id     |
| `name`        | `String`     | 사업장명        |
| `address`     | `String`     | 주소          |
| `zipCode`     | `String`     | 우편번호        |
| `cuisine`     | `String`     | 업태구분명       |
| `phoneNumber` | `String`     | 전화번호        |
| `homepageUrl` | `String`     | 홈페이지 주소     |
| `avgScore`    | `BigDecimal` | 평점          |

**2. 404 Not Found**

```json
HTTP/1.1 404
Content-Type: application/json

{
  "message": "존재하지 않는 멤버입니다."
}
```

</details>

</details>

<details>
<summary>시군구 목록 API</summary>

> 서울시의 시군구 목록을 조회합니다.

```java
GET /api/v1/regions
```

#### Response

**1. 200 Ok**

```json
HTTP/1.1 200
Content-Type: application/json

[
  {
  "city": "서울",
  "district": "강남구"
  },
        ...
  {
  "city": "서울",
  "district": "중랑구"
  }
]
```

| Field      | Type     | Description |
|------------|----------|-------------|
| `city`     | `String` | 시-도        |
| `district` | `String` | 시군구       |
</details>

<details>
<summary>리뷰 생성 API</summary>

> 사용자가 음식점의 리뷰를 생성합니다.

```
POST /api/v1/reviews
```

#### request
| Parameter   | Type     | Default Value           | Description |
| :--------   | :------- | :-------------------    |:------------|
| `memberAccount`    | `String`|     		| 	멤버 계정명		   |
| `restaurantId` | `Long` |    			| 	음식점 id			  |
| `content` | `String` |  ""    			| 	리뷰 내용				  |
| `score` | `Int` | 5     | 	점수         |

#### response

**1. 201 Created**
```json
HTTP/1.1 201
Content-Type: application/json

{
  "message": "리뷰가 생성되었습니다."
}
```

**2. 404 Not Found**
```json
HTTP/1.1 404
Content-Type: application/json

{
  "message" : "존재하지 않는 멤버입니다."
}
```

**3. 404 Not Found**
```json
HTTP/1.1 404
Content-Type: application/json

{
  "message" : "음식점이 존재하지 않습니다."
}
```
</details>

## Related Repository
- Data Pipeline Server: 
[EatMatJib-DataServer](https://github.com/team-TEN-TEN/EatMatJib-DataServer)

## 프로젝트 진행 및 이슈 관리

| 날짜             | 제목          | 주요 내용                                                                                        |
|----------------|-------------|----------------------------------------------------------------------------------------------|
| **2024/08/27** | **데일리 스크럼** | [2차 과제 요구사항 분석](https://sebel.notion.site/2-f0ffe0256531438cae2cb64b73526bf1?pvs=4)          |
| **2024/08/28** | **데일리 스크럼** | [요구사항 명세서 작성 및 ERD 설계](https://sebel.notion.site/ERD-d40bc258a8c943c18c69118d1538e2d4?pvs=4) |
| **2024/08/29** | **정규수업 회의** | [구현방법 논의](https://sebel.notion.site/e087ed0c12c646a5a742b829e3512abe?pvs=4)                  |
| **2024/08/30** | **데일리 스크럼** | [진행사항 확인 - 금](https://sebel.notion.site/127e49c7ce6f4b87aedbe333d96eb5a4?pvs=4)              |
| **2024/08/31** | **데일리 스크럼** | [진행사항 확인 - 토](https://sebel.notion.site/659a7398f32d4743b9cda5bf545abad8?pvs=4)              |
| **2024/09/01** | **데일리 스크럼** | [진행사항 확인 및 이슈 논의 - 일](https://sebel.notion.site/9d6690497a394d8180f3b95c10bac68e?pvs=4)      |
| **2024/09/02** | **데일리 스크럼** | [readme 작성 및 병합](https://sebel.notion.site/readme-40883bb1f1694142b9ab049dc0abed6e?pvs=4)    |

## 구현과정

<details>
<summary>ERD 모델링</summary>

![eatmatjib_erd](https://github.com/user-attachments/assets/a2036a1c-8994-4caf-ac14-fe61bf3cb594)

- member (사용자)
    - 서비스에 등록한 사용자
    - 위경도를 변환한 좌표(x, y) 및 점심 추천 기능 사용 여부 저장
- review (평가)
    - 사용자의 맛집 평가 데이터
- restaurant (음식점)
    - Open API 서울시 일반음식점 데이터
- region (지역)
    - 시군구 csv 데이터에서 시도(city), 시군구(district)를 추출하여 저장

</details>
<details>
<summary>아키텍처 설계</summary>

- 도메인 주도 설계(DDD) 기반의 계층형 아키텍처
    - 프로젝트의 규모가 크지 않기 때문에, 복잡한 아키텍처보다 간단하면서도 효과적인 계층형 아키텍처를 선택
    - 팀원 간 역할 분담이 용이하고, 도메인 로직의 명확한 구현을 위해 DDD 기반의 도메인 패키지 분리

</details>
<details>
<summary>디렉터리 구조</summary>

```plain
├── 📂 server
│   ├── 📂 common
│   │   ├── 📂 config
│   │   ├── 📂 exception
│   │   ├── 📂 model
│   │   └── 📂 util
│   ├── 📂 member
│   │   ├── 📂 controller
│   │   ├── 📂 domain
│   │   ├── 📂 repository
│   │   └── 📂 service
│   ├── 📂 region
│   ├── 📂 restaurant
│   ├── 📂 reiview
```

</details>

## Author

| <img src="https://avatars.githubusercontent.com/u/58517873?v=4" width="150" height="150"/> | <img src="https://avatars.githubusercontent.com/u/114724461?s=400&v=4" width="150" height="150"/> | <img src="https://avatars.githubusercontent.com/u/83827023?v=4" width="150" height="150"/> |
|:------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------:|
|                    김유경<br/>[@YuGyeong98](https://github.com/YuGyeong98)                    |                         백현경<br/>[@hyunkkkk](https://github.com/hyunkkkk)                          |                          이찬미<br/>[@05AM](https://github.com/05AM)                          |
|                              사용자 정보<br/>사용자 설정 업데이트<br/>맛집 목록                              |                                       맛집 상세정보<br/>맛집 평가 생성                                        |                         위경도 좌표 변환<br/>시군구 csv 데이터 DB 저장<br/>시군구 목록                         |