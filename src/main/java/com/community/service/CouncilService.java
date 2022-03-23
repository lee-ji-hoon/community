package com.community.service;

import com.community.web.dto.CouncilForm;
import com.community.domain.account.Account;
import com.community.domain.council.Council;
import com.community.domain.council.CouncilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CouncilService {

    private final CouncilRepository councilRepository;

    public Council saveNewPosts(CouncilForm councilForm, Account account) {
        Council council = Council.builder()
                .postTitle(councilForm.getPostTitle())
                .postLink(councilForm.getPostLink())
                .postContent(councilForm.getPostContent())
                .postTarget(councilForm.getPostTarget())
                .postSort(councilForm.getPostSort())
                .contactNum(councilForm.getContactNum())
                .postWriter(account)
                .pageView(0)
                .eventStartDate(councilForm.getEventStartDate())
                .eventEndDate(councilForm.getEventEndDate())
                .applyPeriodStartDate(councilForm.getApplyPeriodStartDate())
                .applyPeriodEndDate(councilForm.getApplyPeriodEndDate())
                .uploadTime(LocalDateTime.now())
                .build();
        return councilRepository.save(council);
    }

    public List<Council> postSort(String sort) {
        return councilRepository.findAllByPostSortOrderByEventEndDateDesc(sort);
    }

    public List<Council> noticeSort(String sort) {
        return councilRepository.findTop4ByPostSortOrderByUploadTimeDesc(sort);
    }

    public LocalDate nowDate() {
        return LocalDate.now();
    }

    public void viewUpdate(long id, HttpServletRequest request, HttpServletResponse response) {
        Cookie viewCookie=null;
        Cookie[] cookies=request.getCookies();
        log.info("cookie : " + Arrays.toString(cookies));

        if(cookies !=null) {
            for (Cookie cookie : cookies) {
                log.info(cookie.getName());
                //만들어진 쿠키들을 확인하며, 만약 들어온 적 있다면 생성되었을 쿠키가 있는지 확인
                if (cookie.getName().equals("|c" + id + "|")) {
                    log.info("If Cookie Name : " + cookie.getName());
                    //찾은 쿠키를 변수에 저장
                    viewCookie = cookie;
                }
            }
        }else {
            log.info("Cookies Check Logic : None Cookie");
        }
        // 만들어진 쿠키가 없음을 확인
        if(viewCookie==null) {
            log.info("viewCookies Check Logic : None Cookie");
            try {
                // 만들어진 쿠키가 없으므로 조회수 증가
                pageViewUpdate(id);

                // 해당 페이지를 다녀갔다는 쿠키 생성
                Cookie newCookie=new Cookie("|c"+id+"|","readCount");
                response.addCookie(newCookie);
            } catch (Exception e) {
                log.info("input cookie Error : " + e.getMessage());
                e.getStackTrace();
            }
            // 만들어진 쿠키가 있으면 증가로직 진행하지 않음
        }else {
            String value=viewCookie.getValue();
            log.info("viewCookie Check Logic : exist Cookie Value = " + value);
        }
    }

    /* 페이지 조회수 증가 서비스 */
    private void pageViewUpdate(Long cid){
        Council council = councilRepository.findByCid(cid);
        Integer page = council.getPageView();
        council.setPageView(++page);
        councilRepository.save(council);
    }

    // 업데이트
    public Council updateCouncil(Long cid, CouncilForm councilForm) {
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

    }

}
