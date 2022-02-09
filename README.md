# 남서울대학교 멀티미디어 학과 전용 커뮤니티 프로젝트

## 2022 02 08 기초 세팅

- FRONT : 배성현 (HTML / CSS / JS)
- BACKEND : 이지훈, 가준영 (SpringBoot, JPA)

[Notion](https://www.notion.so/c080919d0f1e48c68971c8e96b997072)

[Google Slide](https://docs.google.com/presentation/d/1LgabB2Cvdc6RZfzv0Peh3Mi5cjkarMOXQIVbpFCfW1U/edit#slide=id.p)

[ERD](https://www.erdcloud.com/d/c6fnKuozMTH2YMWaS)

[FlowChart](https://app.diagrams.net/#G16A5Jk-hlk1xUnX1pVzuDqR5D5I43Seua)

### Spring Initializr 세팅 
![image](https://user-images.githubusercontent.com/53300830/152991455-f48272b1-a1b2-4267-aa0f-2948daa15546.png)

### postgress
- create database community;
- create user master with encrypted password '1234';
- grant all privileges on database community to master;

## 2022 02 09 회원가입 및 이메일 토큰 인증 구현
- 회원가입 페이지
![image](https://user-images.githubusercontent.com/53300830/153216855-8a028a5c-bf84-4f64-b8a1-57d3be24f795.png)

- 토큰 인증 전 ql
![image](https://user-images.githubusercontent.com/53300830/153217362-62bb248e-7aa7-48c2-9607-3fb92e8ff3c8.png)
- 이메일 토큰 발급
![image](https://user-images.githubusercontent.com/53300830/153217446-b50f1f6e-3a18-47dc-8ac0-11268612e8ba.png)
- 인증 후 SQL 
![image](https://user-images.githubusercontent.com/53300830/153217931-7994f197-c580-4046-ac0c-5eadb3ba4338.png)
- 인증화면 주소 및 view
![image](https://user-images.githubusercontent.com/53300830/153217627-ef5f55d5-ed85-4098-94f6-b01cf018a1fd.png)





