# 멀티미디어 학과 전용 커뮤니티 프로젝트

### Unect(University + Connect) 프로젝트 (2022 02 08  ~ )  

에브리타임 같이 각 학교 학생들이 모두 소통할 수 있는 커뮤니티 보다 더 작은 개념으로 
우리과의 있는 선후배들이 일면식이 없어도 서로의 정보를 교환하고 친목을 도모할 수 있는 사이트를 만들고 싶다는 생각에서 비롯됨.   

> 특히, 코로나 사태가 장기화됨에 따라 더더욱 같은 과 학생끼리도 친해질 계기가 없었는데   
요즘 세대 학생들이 모두 사용하는 SNS 형태의 사이트로 계기를 마련해주고자 함

#### 📋 구현할 페이지
1. 커뮤니티 게시판
2. 스터디 게시판
3. 장터
4. 학생회
5. 졸업작품
6. 북마크
7. 개개인의 프로필 페이지 

## 🚀  2022 02 08 기초 세팅

- 팀명 : 봄
- 프로젝트 : unect (university + connect)
- 팀원 : 이지훈 (Springboot, JPA, QueryDsl, AWS, MYSQL), 가준영 (SpringBoot, JPA, MYSQL)

[Notion](https://www.notion.so/c080919d0f1e48c68971c8e96b997072)

[Google Slide](https://docs.google.com/presentation/d/1LgabB2Cvdc6RZfzv0Peh3Mi5cjkarMOXQIVbpFCfW1U/edit#slide=id.p)

[ERD](https://www.erdcloud.com/d/c6fnKuozMTH2YMWaS)

[FlowChart](https://app.diagrams.net/#G16A5Jk-hlk1xUnX1pVzuDqR5D5I43Seua)

### Spring Initializr 시작 세팅 
![image](https://user-images.githubusercontent.com/53300830/152991455-f48272b1-a1b2-4267-aa0f-2948daa15546.png)

## 🚀  개발 환경
### Back-end
Spring Boot / Spring Data JPA / Spring Security / QueryDsl / Gradle

### DataBase
MySQL, H2

### Infra
[Amazon] EC2, ROUTE53, RDS, CloudFront, S3

## 🚀  프로젝트 구조
![image](https://user-images.githubusercontent.com/53300830/167577841-e9cb3513-2a5f-40b4-81ae-af93bdc64c89.png) ![image](https://user-images.githubusercontent.com/53300830/167578982-066e6642-b7a4-4c6f-ad15-97697aea903d.png)


> 프로젝트 설계를 진행하면서 레이어를 우선으로할지 고민하다가 둘 다 적용해보고 결정하기로 했다.  
> 그 결과 우리는 레이어를 우선으로 하는게 좀 더 보기 편하다고 느껴져 위 처럼 구성했다.

## 🚀  ERD
![dev community](https://user-images.githubusercontent.com/53300830/167578110-b77870a8-70c7-4582-ad90-7755124a4853.png)

## 🚀  추가적인 구현 사항
- AWS EC2를 이용해서 이미지 업로드
- HTTPS에 필요한 SSL 인증서를 AWS에서 발급 후 도메인 적용 (https://unect.cf/)
- Ajax를 이용한 비동기 통신으로 댓글 좋아요등 사용자에게 편리하게 구현

## 🚀  졸업작품 진행 상황

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
[[Google Slide](https://docs.google.com/presentation/d/1jxpAf2MXjjL9CIVvUULy3Go33thGOWZvqlOCQ48JEBc/edit#slide=id.p)]

[[Notion](https://www.notion.so/3-e4a49e39cf5b413caa273f1f669c0cf6)]

#### 결과
- 통과 했으며, 현재 기준에서 더이상 일 벌리지말고 유효성 검사에 집중하며 버그 잡을 것

