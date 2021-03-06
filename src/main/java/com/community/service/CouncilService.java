package com.community.service;

import com.community.infra.aws.S3;
import com.community.infra.aws.S3Repository;
import com.community.infra.aws.S3Service;
import com.community.domain.account.Account;
import com.community.domain.council.Council;
import com.community.domain.council.CouncilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CouncilService {

    private final CouncilRepository councilRepository;
    private final S3Repository s3Repository;
    private final S3Service s3Service;

    public Council saveNewPosts(List<MultipartFile> multipartFile, Account account, String postSort, String postTarget,
                                String postTitle, String postLink, String contactNum,
                                LocalDateTime applyPeriodStartDate, LocalDateTime applyPeriodEndDate,
                                LocalDateTime eventStartDate, LocalDateTime eventEndDate, String postContent) {
        Council council = Council.builder()
                .postWriter(account)
                .postSort(postSort)
                .postTarget(postTarget)
                .postTitle(postTitle)
                .postLink(postLink)
                .contactNum(contactNum)
                .applyPeriodStartDate(applyPeriodStartDate)
                .applyPeriodEndDate(applyPeriodEndDate)
                .eventStartDate(eventStartDate)
                .eventEndDate(eventEndDate)
                .postContent(postContent)
                .uploadTime(LocalDateTime.now())
                .pageView(0)
                .build();

        uploadImage(multipartFile, council);

        return councilRepository.save(council);
    }

    private void uploadImage(List<MultipartFile> multipartFile, Council council) {
        String uploadFolder = "council-img/";

        if (multipartFile != null) {
            List<String> imageFileList = s3Service.upload(multipartFile, uploadFolder);

            for (String imageFileName : imageFileList) {
                S3 s3 = new S3();
                s3.setImageName(uploadFolder + imageFileName);
                s3.setImagePath(S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + uploadFolder + imageFileName);
                s3.setCouncil(council);

                S3 s3Image = s3Repository.save(s3);

                log.info("graduation Image :{}", council.getImageList());
                log.info("s3Image : {}", s3Image);
            }
        }
    }

    public void deleteCouncil(Council council) {
        List<S3> imageList = council.getImageList(); // ????????? ????????????

        for (S3 s3 : imageList) s3Service.deleteFile(s3.getImageName()); // ????????? ??????

        councilRepository.delete(council);
    }

    public List<Council> postSort(String sort) {
        return councilRepository.findAllByPostSortOrderByEventEndDateDesc(sort);
    }

    public LocalDateTime nowDate() {
        return LocalDateTime.now();
    }

    public void viewUpdate(long id, HttpServletRequest request, HttpServletResponse response) {
        Cookie viewCookie=null;
        Cookie[] cookies=request.getCookies();
        log.info("cookie : " + Arrays.toString(cookies));

        if(cookies !=null) {
            for (Cookie cookie : cookies) {
                log.info(cookie.getName());
                //???????????? ???????????? ????????????, ?????? ????????? ??? ????????? ??????????????? ????????? ????????? ??????
                if (cookie.getName().equals("|c" + id + "|")) {
                    log.info("If Cookie Name : " + cookie.getName());
                    //?????? ????????? ????????? ??????
                    viewCookie = cookie;
                }
            }
        }else {
            log.info("Cookies Check Logic : None Cookie");
        }
        // ???????????? ????????? ????????? ??????
        if(viewCookie==null) {
            log.info("viewCookies Check Logic : None Cookie");
            try {
                // ???????????? ????????? ???????????? ????????? ??????
                pageViewUpdate(id);

                // ?????? ???????????? ??????????????? ?????? ??????
                Cookie newCookie=new Cookie("|c"+id+"|","readCount");
                response.addCookie(newCookie);
            } catch (Exception e) {
                log.info("input cookie Error : " + e.getMessage());
                e.getStackTrace();
            }
            // ???????????? ????????? ????????? ???????????? ???????????? ??????
        }else {
            String value=viewCookie.getValue();
            log.info("viewCookie Check Logic : exist Cookie Value = " + value);
        }
    }

    /* ????????? ????????? ?????? ????????? */
    private void pageViewUpdate(Long cid){
        Council council = councilRepository.findByCid(cid);
        Integer page = council.getPageView();
        council.setPageView(++page);
        councilRepository.save(council);
    }

    // ????????????
    /*public Council updateCouncil(Long cid, CouncilForm councilForm) {
        Council council = councilRepository.findByCid(cid);
        council.setPostSort(councilForm.getPostSort());
        council.setPostTitle(councilForm.getPostTitle());
        council.setPostTarget(councilForm.getPostTarget());
        council.setPostLink(councilForm.getPostLink());
        council.setContactNum(councilForm.getContactNum());
        council.setPostContent(councilForm.getPostContent());
        council.setEventStartDate(councilForm.getEventStartDate());
        council.setEventEndDate(councilForm.getEventEndDate());
        council.setApplyPeriodStartDate(councilForm.getApplyPeriodStartDate());
        council.setApplyPeriodEndDate(councilForm.getApplyPeriodEndDate());
        return councilRepository.save(council);

    }*/
    public void updateCouncil(Council council, List<MultipartFile> multipartFile,
                                 String postSort, String postTarget,
                                 String postTitle, String postLink, String contactNum,
                                 LocalDateTime applyPeriodStartDate, LocalDateTime applyPeriodEndDate,
                              LocalDateTime eventStartDate, LocalDateTime eventEndDate, String postContent) {

        council.setPostSort(postSort);
        council.setPostTitle(postTitle);
        council.setPostTarget(postTarget);
        council.setPostLink(postLink);
        council.setContactNum(contactNum);
        council.setPostContent(postContent);
        council.setApplyPeriodStartDate(applyPeriodStartDate);
        council.setApplyPeriodEndDate(applyPeriodEndDate);
        council.setEventStartDate(eventStartDate);
        council.setEventEndDate(eventEndDate);

        uploadImage(multipartFile, council);
    }

}
