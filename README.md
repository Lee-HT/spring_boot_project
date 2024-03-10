# 블로그용 Rest API

### 1인 프로젝트
#### 블로그를 구현할 때 필요한 Rest API 를 구현 해보기 위해 프로젝트를 개발하게 되었습니다.


## 개발환경
Spring Boot (3.1.2)
JAVA 

#### 사용 IDE
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


## Oauth2

### 구글 oauth2 를 통한 로그인 과정
![oauth2](/Image/codelia_oauth2.png)

#### refreshToken (7d)
쿠키 (HttpOnly, Secure) 에 저장
CSRF 에 취약 XSS 에 상대적으로 안전

#### accessToken (24h)
sessionStorage 에 저장 , Authorization Header 로 전송
XSS 에 취약 CSRF 에 안전

### 프론트와 Origin 이 다르기 때문에 Cors 설정

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
Redis Cache 를 적용해 조회 성능을 개선 하였습니다

### ngrinder 성능 테스트

Redis Cache 를 적용 한 후에 최근 게시글 조회 메소드 요청의
평균 테스트 시간이 264.72ms 에서 7.64ms 로 감소하였고
TPS 또한 20배 증가하는 것을 확인 하였습니다.

#### 캐시 적용 전

![cache_not](/Image/redis/codelia_cache_not_apply.png)


#### 캐시 적용 후

![cache_apply](/Image/redis/codelia_cache_apply.png)