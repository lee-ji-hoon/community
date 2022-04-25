package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.board.Board;
import com.community.domain.inquire.Inquire;
import com.community.domain.inquire.InquireRepository;
import com.community.infra.aws.S3;
import com.community.infra.aws.S3Repository;
import com.community.infra.aws.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InfoPageService {

    private final S3Repository s3Repository;
    private final InquireRepository inquireRepository;
    private final S3Service s3Service;

    public Inquire saveNewInquire(List<MultipartFile> multipartFile, Account account,
                                  String contact_title, String contact_content) {
        Inquire inquire = Inquire.builder()
                .account(account)
                .title(contact_title)
                .content(contact_content)
                .uploadTime(LocalDateTime.now())
                .isAnswered(false)
                .build();

        uploadImage(multipartFile, inquire);

        return inquireRepository.save(inquire);
    }

    public void deleteInquire(Inquire inquire) {
        List<S3> imageList = inquire.getImageList(); // 이미지 불러오기

        for (S3 s3 : imageList) s3Service.deleteFile(s3.getImageName()); // 이미지 삭제

        inquireRepository.delete(inquire);
    }

    private void uploadImage(List<MultipartFile> multipartFile, Inquire inquire) {
        String uploadFolder = "contact-img/";

        if (multipartFile != null) {
            List<String> imageFileList = s3Service.upload(multipartFile, uploadFolder);

            for (String imageFileName : imageFileList) {
                S3 s3 = new S3();
                s3.setImageName(uploadFolder + imageFileName);
                s3.setImagePath(S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + uploadFolder + imageFileName);
                s3.setInquire(inquire);

                S3 s3Image = s3Repository.save(s3);

                log.info("graduation Image :{}", inquire.getImageList());
                log.info("s3Image : {}", s3Image);
            }
        }
    }

    public void deleteImage(S3 s3) {
        s3Repository.delete(s3);
        s3Service.deleteFile(s3.getImageName());
    }
}
