# 블로그용 Rest API

### 1인 프로젝트
#### 블로그를 구현할 때 필요한 Rest API 를 구현 해보기 위해 프로젝트를 개발하게 되었습니다.

### 배포 URL
#### https://codelia.shop/api

## 개발환경
Spring Boot (3.1.2)
JAVA (17)

#### IDE
Intellij

#### 데이터베이스
Mysql

#### 인프라
AWS RDS
AWS EC2
AWS S3

#### CI/CD
Github Actions
AWS CodeDeploy


## ERD

### ERDCloud
![ERD](/Image/codelia_erd.png)

## CI/CD




## Authentication

#### 별도의 Id,Pw 저장 없이 Oauth2 만을 이용한 로그인으로 인증하도록 하였습니다.

#### Provider : Google, Naver

### Oauth2

#### 무 상태성을 위해 최초 로그인 시에 provider 로 인증, 인가 후 유저 정보를 jwt 에 저장한 뒤
#### 매번 재인증 없이 Jwt 를 사용하여 유저를 인증, 식별

### 구글 oauth2 를 통한 로그인 과정
![oauth2](/Image/codelia_oauth2.png)

#### refreshToken (7d)
쿠키 (HttpOnly, Secure) 에 저장
CSRF 에 취약 XSS 에 상대적으로 안전

#### accessToken (24h)
sessionStorage 에 저장 , Authorization Header 로 전송
XSS 에 취약 CSRF 에 안전

### 프론트와 Origin 이 달라 교차 출처 리소스 공유를 위해 CORS 설정

#### WebMvc
![Cors](/Image/cors/codelia_webmvc_cors.png)

#### Spring Security
![Cors](/Image/cors/codelia_security_cors.png)


## API

### Controller, Service, Repository 단위 테스트 코드 작성

![testCode](/Image/codelia_testcode.png)

### API 문서화를 위해 RestDocs 사용

#### 코드 반복을 줄이기 위해 공통 부분을 분리 하였습니다.

문서 이름 클래스 & 메소드 이름 으로 설정, pretty json 적용, 호스트 및 포트 표기

![restDocs](/Image/restdocs/codelia_restdocs_config.png)

RestDocsConfig 추가, 공통 상속 클래스 선언, MockMvc 커스터마이징

![restDocs](/Image/restdocs/codelia_restdocs_setup.png)


#### RestDocs 문서 Uri

배포 주소 https://codelia.shop/api/docs/index.html

로컬 주소 http://localhost:6550/api/docs/index.html

![restDocs](/Image/restdocs/codelia_restdocs_post.png)


## Redis

성능 개선을 위해 자주 요청될 것으로 예상되는
최근 게시글 목록, 최근 요청된 단일 게시글, 카테고리 목록에 대해
Redis Cache 를 적용해 조회 성능을 개선

### ngrinder 성능 테스트

Redis Cache 를 적용 한 후에 최근 게시글 조회 메소드 요청의
평균 테스트 시간이 264.72ms 에서 7.64ms 로 감소하였고
TPS 또한 20배 정도 증가하는 것을 확인

#### 캐시 적용 전

![cache_not](/Image/redis/codelia_cache_not_apply.png)


#### 캐시 적용 후

![cache_apply](/Image/redis/codelia_cache_apply.png)


## JPA N + 1

연관관계가 있는 엔티티 목록을 조회 할 때 연관된 엔티티들을 하나씩 조회하여
n + 1 만큼 쿼리 수행으로 성능이 감소되는 문제가 있음
->
batch 단위로 연관 엔티티를 한번에 조회하여 해결

### batch_size 적용 전

게시글 엔티티들 조회 시 연관된 유저 엔티티 만큼 조회 (n + 1)

![jpa_n+1](/Image/jpa/codelia_jpa_n+1.png)

### batch_size 적용 후

게시글 엔티티들 조회 시 연관된 유저 엔티티를 batch 로 묶어서 조회 ( ( n // batch_size ) + 2)

![jpa_batch](/Image/jpa/codelia_jpa_batchsize.png)


## Google Translation API

### Google Cloud translation 라이브러리

#### API

![translate_api](/Image/translate/codelia_translate_api.png)

#### 게시글 내용 번역 post 메소드 request ->
#### 텍스트 문자열 언어 인식 ( Translation API ) ->
#### 텍스트 번역 (Translation API) ->
#### 번역 된 테스트 응답

### Detect language

![detection_language](/Image/translate/codelia_detection_language.png)

### Translate text

![translate_text](/Image/translate/codelia_translate_text.png)