# 내일배움캠프 일정 관리 앱 만들기


## 개발 환경
- IntellJ IDEA Ultimate Edition 2024.3.1.1

- Java 17.0.11

- Github

- docker

- git 2.34.1

- ubuntu Ubuntu 22.04.5 LTS 64bit


## 개발자 블로그

    https://strnetwork.tistory.com/

### ERD

![image](https://github.com/user-attachments/assets/7d653971-c528-4a8c-8fe7-224e2470bdaf)

### 사이트 주소

    https://www.erdcloud.com/p/JFCDyFXME4XfdTKAc

### API 명세서

|기능|URL|request|response|COMMENT|정상응답|잘못된 응답|
|---|---|---|---|-------|------|-------|
|일정 등록|POST|/api/schedules|요청 body|등록 정보|201: 정상등록|400: 잘못된 요청|
|일정 조회|GET|/api/schedules/{scheduleId}|요청 param|단건 응답 정보|200: 정상조회|404: NOT_FOUND|
|일정 목록 조회|GET|/api/schedules|요청 param|다건 응답 정보|200: 정상조회|-|
|일정 수정|PUT|/api/schedules/{scheduleId}|요청 body|수정 정보|200: 정상수정|404: NOT_FOUND|
|일정 삭제|DELETE|/api/schedules/{scheduleId}|요청 param|-|200: 정상삭제|404: NOT_FOUND|
|회원 등록|POST|/api/authors/join|요청 body|등록 정보| (200)정상등록|(400)잘못된 요청|
|회원 조회|GET|/api/authors/{authorId}|요청 body|단건 응답 정보|(200)정상조회|404: NOT_FOUND|
|회원 이름 수정|PATCH|/api/authors/{authorId}|요청 body|수정 정보|200: 정상수정|404: NOT_FOUND, 400: BAD_REQUEST|
|회원 삭제|DELETE|/api/authors/{authorId}|요청 body|-|200: 정상삭제|400: BAD_REQUEST|


## 프로그램 사용법

    git clone https://github.com/mixedsider/ScheduleProject.git

./ScheduleProject/src/main/java/com/example/scheduleproject/ScheduleProjectApplication.java 에 있는 main 을 실행시키면 됩니다.

