package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.inquire.Inquire;
import com.community.domain.inquire.InquireRepository;
import com.community.domain.notice.Notice;
import com.community.domain.notice.NoticeRepository;
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
    private final NoticeRepository noticeRepository;
    private final S3Service s3Service;

    /* 공지사항 시작 */
    public Notice saveNewNotice(List<MultipartFile> multipartFile, Boolean notice_topCheck,
                                  String notice_title, String notice_content) {
        Notice notice = Notice.builder()
                .noticeTitle(notice_title)
                .noticeContent(notice_content)
                .topSetting(notice_topCheck)
                .uploadTime(LocalDateTime.now())
                .build();

        uploadNoticeImage(multipartFile, notice);

        return noticeRepository.save(notice);
    }

    public void deleteNotice(Notice notice) {
        List<S3> imageList = notice.getImageList(); // 이미지 불러오기

        for (S3 s3 : imageList) s3Service.deleteFile(s3.getImageName()); // 이미지 삭제

        noticeRepository.delete(notice);
    }

    private void uploadNoticeImage(List<MultipartFile> multipartFile, Notice notice) {
        String uploadFolder = "notice-img/";

        if (multipartFile != null) {
            List<String> imageFileList = s3Service.upload(multipartFile, uploadFolder);

            for (String imageFileName : imageFileList) {
                S3 s3 = new S3();
                s3.setImageName(uploadFolder + imageFileName);
                s3.setImagePath(S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + uploadFolder + imageFileName);
                s3.setNotice(notice);

                S3 s3Image = s3Repository.save(s3);
            }
        }
    }
    public void updateNotice(Notice notice, List<MultipartFile> multipartFile,
                             Boolean notice_topCheck,
                             String notice_title, String notice_content) {

        notice.setTopSetting(notice_topCheck);
        notice.setNoticeTitle(notice_title);
        notice.setNoticeContent(notice_content);

        uploadNoticeImage(multipartFile, notice);
    }

    /* 공지사항 끝 */

    /* 문의사항 시작 */
    public Inquire saveNewInquire(List<MultipartFile> multipartFile, Account account,
                                  String contact_title, String contact_content) {
        Inquire inquire = Inquire.builder()
                .account(account)
                .title(contact_title)
                .content(contact_content)
                .uploadTime(LocalDateTime.now())
                .answerTime(null)
                .isAnswered(false)
                .build();

        uploadInquireImage(multipartFile, inquire);

        return inquireRepository.save(inquire);
    }

    public void deleteInquire(Inquire inquire) {
        List<S3> imageList = inquire.getImageList(); // 이미지 불러오기

        for (S3 s3 : imageList) s3Service.deleteFile(s3.getImageName()); // 이미지 삭제

        inquireRepository.delete(inquire);
    }

    private void uploadInquireImage(List<MultipartFile> multipartFile, Inquire inquire) {
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

    /* 문의사항 끝 */

    public void deleteImage(S3 s3) {
        s3Repository.delete(s3);
        s3Service.deleteFile(s3.getImageName());
    }
}
