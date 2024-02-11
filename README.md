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
![oauth2](/Image/codelia_oauth2.png)
#### 로그인 완료 후 refreshToken 을 http only, secure 쿠키에 저장
#### accessToken 은 sessionStorage 에 저장


### API
