# 블로그용 Rest API

### 1인 프로젝트
#### 블로그를 구현할 때 필요한 API 를 개발해보기 위해 프로젝트를 개발하게 되었습니다.


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

## 주요 내용

### ERD

### 로그인

#### 구글 oauth2 를 통한 로그인 과정
![oauth2](/Image/codelia_oauth2.png)

#### refreshToken (7d)
쿠키 (HttpOnly, Secure) 에 저장
CSRF 에 취약 XSS 에 상대적으로 안전

#### accessToken (24h)
sessionStorage 에 저장 , Authorization Header 로 전송
XSS 에 취약 CSRF 에 안전


### API

#### Controller, Service, Repository 테스트 코드 작성

![oauth2](/Image/codelia_testcode.png)

#### API 문서화를 위해 RestDocs 사용
