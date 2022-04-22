# 남서울대학교 멀티미디어 학과 전용 커뮤니티 프로젝트

## 2022 02 08 기초 세팅

- 팀명 : 봄
- 프로젝트 : unect (university + connect)
- 팀원 : 이지훈 (Springboot, JPA, QueryDsl), 가준영 (SpringBoot, JPA)

[Notion](https://www.notion.so/c080919d0f1e48c68971c8e96b997072)

[Google Slide](https://docs.google.com/presentation/d/1LgabB2Cvdc6RZfzv0Peh3Mi5cjkarMOXQIVbpFCfW1U/edit#slide=id.p)

[ERD](https://www.erdcloud.com/d/c6fnKuozMTH2YMWaS)

[FlowChart](https://app.diagrams.net/#G16A5Jk-hlk1xUnX1pVzuDqR5D5I43Seua)

### Spring Initializr 시작 세팅 
![image](https://user-images.githubusercontent.com/53300830/152991455-f48272b1-a1b2-4267-aa0f-2948daa15546.png)

### MYSQL // h2
- CREATE DATABASE community;
- CREATE USER master@'%' IDENTIFIED BY 'nsuspring';
- GRANT ALL PRIVILEGES ON community.* TO master@'%' WITH GRANT OPTION;

<hr/>

### 1차 기획안 발표 3/16(수)
[[Google Slide](https://docs.google.com/presentation/d/11x7zjGIO5-ZYY59PbaNRUh9odlZ44SJzNyocKh50_XQ/edit#slide=id.g11d3f6e9f66_0_977)]

[[Notion](https://www.notion.so/15886ef901a64ed9b2c1b325609bc8bf)]

#### 결과
- 전체적인 호평이 있었으며, 이대로 쭉 진행하면 될 것 같음

#### 이후 방향
- 이미지 등록 방법을 바꿔야 함 + 다른 DB 고려
- 웹 소캣통신 방법 공부하기 (알림, 쪽지 or 실시간 통신 기능 구현에 필요)
<hr/>

### 2차 프로토타입 발표 4/6(수)
[[Google Slide](https://docs.google.com/presentation/d/1xiu76b_hTz6oaf7pMc94rmpDmaVeuDCq1kDCDMBgOZE/edit#slide=id.g11ac4265aca_1_0)]

[[Notion](https://www.notion.so/15886ef901a64ed9b2c1b325609bc8bf)]

#### 결과
- 피피티 및 발표가 조금 아쉬웠음
- 기능자체는 문제가 없음 예정대로 개발 진행 예정

#### 이후 방향
- 자잘한 버그 수정 및 발표 상태 점검

<hr/>

### 3차 프로토타입 발표 4/27(수)


