package com.community.infra.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
@Slf4j
public class S3Service {
    private AmazonS3 s3Client;

    // @Value lombok 아님
    // perperties 값 가져옴
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    public static String CLOUD_FRONT_DOMAIN_NAME = "https://d2c5shumcf1jv0.cloudfront.net";

    @PostConstruct
    public void setS3Client() {
        // 자격증명
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)) // 자격 증명 객체 얻기
                .withRegion(this.region) // client갖고옴
                .build();
    }

    public List<String> upload(List<MultipartFile> files, String folderPath) {
        List<String> fileNameList = new ArrayList<>();

        files.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucket, folderPath + fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }

            fileNameList.add(fileName);
        });
        return fileNameList;
    }

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

    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(this.bucket, fileName);
        boolean isExistObject = s3Client.doesObjectExist(bucket, fileName);

        log.info("deleteObject : {}", deleteObjectRequest.getKey());
        log.info("삭제 될 object : {}", deleteObjectRequest);
        log.info("해당하는 파일 있는지 체크 값 : {}",isExistObject);
        log.info("bucket : {} || fileName : {}", bucket, fileName);

        if (isExistObject) {
            log.info("삭제 실행");
            s3Client.deleteObject(bucket, fileName);
        }
    }
}
