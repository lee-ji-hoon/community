package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.graduation.Graduation;
import com.community.domain.graduation.GraduationRepository;
import com.community.infra.aws.S3;
import com.community.infra.aws.S3Repository;
import com.community.infra.aws.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GraduationService {

    private final GraduationRepository graduationRepository;
    private final S3Repository s3Repository;
    private final S3Service s3Service;

    public Graduation createNewGraduationProject(List<MultipartFile> multipartFile, String title,
                                           String teamMember, String teamName,
                                           String path, String graduationType,
                                           int graduationDate, String description, Account account) {



        Graduation graduation = Graduation.builder()
                .title(title)
                .teamMember(teamMember)
                .teamName(teamName)
                .path(path)
                .graduationType(graduationType)
                .graduationDate(graduationDate)
                .description(description)
                .account(account)
                .uploadTime(LocalDateTime.now())
                .build();
        

        uploadImage(multipartFile, graduation);

        return graduationRepository.save(graduation);
    }

    public void deleteGraduation(Graduation graduation) {
        List<S3> imageList = graduation.getImageList(); // 이미지 불러오기

        for (S3 s3 : imageList) s3Service.deleteFile(s3.getImageName()); // 이미지 삭제

        graduationRepository.delete(graduation);
    }

    public void updateGraduation(Graduation graduation, List<MultipartFile> multipartFile, String title,
                                 String teamMember, String teamName, String path, String graduationType,
                                 int graduationDate, String description) {

        graduation.setTitle(title);
        graduation.setTeamMember(teamMember);
        graduation.setTeamName(teamName);
        graduation.setPath(path);
        graduation.setGraduationType(graduationType);
        graduation.setGraduationDate(graduationDate);
        graduation.setDescription(description);

        uploadImage(multipartFile, graduation);
    }

    private void uploadImage(List<MultipartFile> multipartFile, Graduation graduation) {
        String uploadFolder = "graduation-img/";

        if (multipartFile != null) {
            List<String> imageFileList = s3Service.upload(multipartFile, uploadFolder);

            for (String imageFileName : imageFileList) {
                S3 s3 = new S3();
                s3.setImageName(uploadFolder + imageFileName);
                s3.setImagePath(S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + uploadFolder + imageFileName);
                s3.setGraduation(graduation);

                S3 s3Image = s3Repository.save(s3);

                log.info("graduation Image :{}", graduation.getImageList());
                log.info("s3Image : {}", s3Image);
            }
        }
    }
}
