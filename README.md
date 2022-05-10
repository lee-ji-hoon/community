# 멀티미디어 학과 전용 커뮤니티 프로젝트

### Unect(University + Connect) 프로젝트 (2022 02 08  ~ )  

에브리타임 같이 각 학교 학생들이 모두 소통할 수 있는 커뮤니티 보다 더 작은 개념으로 
우리과의 있는 선후배들이 일면식이 없어도 서로의 정보를 교환하고 친목을 도모할 수 있는 사이트를 만들고 싶다는 생각에서 비롯됨.   

> 특히, 코로나 사태가 장기화됨에 따라 더더욱 같은 과 학생끼리도 친해질 계기가 없었는데   
요즘 세대 학생들이 모두 사용하는 SNS 형태의 사이트로 계기를 마련해주고자 함

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

## ⛔️ 프로젝트 진행 중 생겼던 이슈들과 해결책
- 여러 이슈가 있었는데, 그 중 인상깊었던 이슈들을 3개 정도를 서술했습니다.
### ✅ 이슈 1. DB에 프로필 이미지를 data형태로 저장하게 되면서 생긴 성능 저하 해결
- 개발할 때 당장의 속도만 중시하다보니 성능을 신경안쓴채 이미지를 data형태로 DB에 저장하면서 생긴 이슈이다.  
- 이것 때문이 아니더라도 이후에 다양한 페이지에 올라갈 이미지들 때문에라도 해결했어야 했다.
```java
// 프로필 이미지 변경 요청
@PostMapping("/profile/settings/profile-img")
public String updateProfileImageForm(@CurrentUser Account account,
                                     @RequestPart(required = false, value = "file") MultipartFile file,
                                        RedirectAttributes redirectAttributes) throws IOException {
    String imageKey = account.getProfileImageKey();
    // 원래 이미지 삭제
    if (imageKey != null) {
        log.info("profile 원래 이미지 삭제 : {}", imageKey);
        s3Service.deleteFile(imageKey);
    }
    String folderPath = "profile-img/";

    String profileImageKey = s3Service.upload(file, folderPath); // s3 이미지 업로드
    String profile = S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + folderPath + profileImageKey; // 디비에 저장하고 접근할 주소

    log.info("profileImage : {}", profile);
    profileService.updateProfileImage(account, profile, profileImageKey, folderPath);
    redirectAttributes.addFlashAttribute("message", "프로필이미지를 수정했습니다.");

    return "redirect:" + "/profile/settings/profile-img";
}
```
> 디비에 저장하는 방식 대신 이미지를 S3에 저장후 해당 접근 URL을 DB에 저장해서 불러오는 방식으로 해결했다.
<hr/>

### ✅ 이슈 2. S3 이미지 업로드 중 동일한 이름의 파일로 인해 오류 발생
- 이미지 이름이 같은 경우를 생각 못하고 넘어온 이미지 이름 그대로 사용 해서 생긴 문제
```java
public String upload(MultipartFile file, String folderPath) throws IOException, IllegalArgumentException {

    String fileName = createFileName(file.getOriginalFilename());

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setContentType(file.getContentType());

    // 업로드를 위해 사용되는 함수 (참고 https://docs.aws.amazon.com/ko_kr/AmazonS3/latest/userguide/upload-objects.html)
    s3Client.putObject(new PutObjectRequest(bucket, folderPath + fileName, file.getInputStream(), objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead)); // 외부에 공개되는 이미지이므로 read 권한 주기
    return fileName;
}

// 기존 확장자는 유지한채 유니크한 파일 이름 생성 로직
private String createFileName(String originalFilename) {
    return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
}

// 파일의 확자명을 가져오는 로직
private String getFileExtension(String fileName) {
    try {
        return fileName.substring(fileName.lastIndexOf("."));
    } catch (StringIndexOutOfBoundsException e) {
        throw new IllegalArgumentException(String.format("잘못된 형식의 파일 ($s) 입니다", fileName));
    }
}
```
> 기존 확장자 + randUUID()를 이용해서 파일 이름을 유니크하게 만들어서 해결했다.

<hr/>

### ✅ 이슈 3. 알림 발송과 Ajax 통신 사이의 최적화 문제
- 스터디에서 관심분야와 동일한 유저들에게 알림을 보내는 과정에서 생긴 문제
- 비동기 통신이므로 버튼 클릭시 status 은 success 반환하므로 알림 전송에 성공했다고 메시지 띄우게 된다.
- 하지만 이메일 전송이 문제가 됐다. 
> 그 이유는 이메일 발송 경우에는 딜레이가 있기 때문에 알람 전송에 성공했다고 메시지가 뜨더라도 페이지 이동시 문제가 된다.
```java
@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        // 새로운 thread 실행 -> 최대한 비동기 통신에 바람직한 방법
        // thread 새롭게 찍히는걸 로그에서 확인 가능
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processors : {}", processors);

        // 새로운 알람 들어왔을 때 처리하는 일꾼들 및 도구들
        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors * 2);
        executor.setQueueCapacity(50);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("Async");
        executor.initialize(); // 실행

        return executor;
    }
}
```

> 비동기 통신에 걸맞게 ThreadPool을 이용한 비동기(Asynchronous)를 사용해서 해결했다. 

```java
@Async
public class StudyEventListener {
  // 생략
  
  @EventListener
    public void studyCreate(StudyCreatedPublish studyCreatedPublish) {
  }
}
```

## 📋  프로젝트 진행과 끝맺음
### ✅  1. 협업 과정에서 팀원과의 원만한 의사소통
- 팀프로젝트이므로 혼자가 아닌 팀원과 같이 하는 것이기 때문에 어떠한 것을 하던 팀원과 우선 이야기를 한 뒤 진행을 했습니다.
- 매주 일요일 오전10시부터 오후 10시까지 디스코드로 서로의 화면을 공유해서 문제가 생길때마다 공유하고 같이 해결해 나가는 짝 프로그래밍을 실천했습니다.  

![image](https://user-images.githubusercontent.com/53300830/167591219-a9a73473-f46f-4000-a1f8-658c5c935ffd.png)

### ✅  2. 서비스 개발부터 배포까지
- 프로젝트를 처음 개발 하면서 내가 직접 배포까지 해야지 전반적인 개념과 구조를 익힐 수 있다고 생각했기 때문에 배포까지 진행해보았습니다.  
[사이트 주소](https://unect.cf/)

### ✅  3. 성능 향상
- 블로그나 공식문서에서 코드를 갖고 와서 쓰더라도 이걸 왜 쓰는지 한 번 더 생각해보며 어디에 적용할지 생각하며 했습니다.
- 페이징 처리 / ThreadPool을 이용한 비동기(Asynchronous) 등

### ⛔️  프론트 개발자의 부재
- 기획서를 작성까지는 프론트 개발자가 있었지만 개인사정으로 인해 2명의 개발자로만 개발을 하게 됐습니다.
- 그 결과 프론트까지 하기에는 시간이 부족하다고 판단이 돼 themeforest에서 템플릿을 산 뒤 진행했습니다.
