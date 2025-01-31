### ERD

![image](https://github.com/user-attachments/assets/504329d1-af98-4d6e-bfc1-879a838ad297)


### API 명세서

|기능|URL|request|response|상태코드|
|---|---|---|---|-------|
|일정 등록|POST|/api/schedules|요청 body|등록 정보| 200 : 정상등록 400: 잘못된 요청|
|일정 조회|GET|/api/schedules/{scheduleId}|요청 param|단건 응답 정보|200: 정상조회 404: NOT_FOUND|
|일정 목록 조회|GET|/api/schedules|요청 param|다건 응답 정보|200: 정상조회|
|일정 수정|PUT|/api/schedules/{scheduleId}|요청 body|수정 정보|200: 정상수정 404: NOT_FOUND|
|일정 삭제|DELETE|/api/schedules/{scheduleId}|요청 param|-|200: 정상삭제 404: NOT_FOUND|
