package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountType;
import com.community.domain.account.CurrentUser;
import com.community.domain.inquire.Inquire;
import com.community.domain.inquire.InquireAnswer;
import com.community.domain.inquire.InquireAnswerRepository;
import com.community.domain.inquire.InquireRepository;
import com.community.domain.notice.Notice;
import com.community.domain.notice.NoticeRepository;
import com.community.infra.aws.S3;
import com.community.infra.aws.S3Repository;
import com.community.service.InfoPageService;
import com.community.web.dto.InquireAnswerForm;
import com.community.web.dto.ReplyForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InfoPageController {

    private final InfoPageService infoPageService;
    private final InquireRepository inquireRepository;
    private final InquireAnswerRepository inquireAnswerRepository;
    private final NoticeRepository noticeRepository;
    private final S3Repository s3Repository;

    @GetMapping("/info/about")
    public String aboutPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "info/info-about";
    }

    /* 공지사항 시작 */
    @GetMapping("/info/notice")
    public String boardTypeList(@CurrentUser Account account, Model model,
                                @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                        direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Notice> notices = noticeRepository.findAllByOrderByUploadTimeDesc(pageable);

        model.addAttribute("pageNo", page);
        model.addAttribute("notices", notices);
        model.addAttribute(account);

        return "info/info-notice";
    }

    @ResponseBody
    @RequestMapping(value = "/notice-new", method = RequestMethod.POST)
    public Long contactFormSubmit(@CurrentUser Account account,
                                  @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile,
                                  @RequestParam(value = "notice_topCheck", required = false) String notice_topCheck,
                                  @RequestParam(value = "notice_title", required = false) String notice_title,
                                  @RequestParam(value = "notice_content", required = false) String notice_content) {
        log.info("notice_topCheck = {}", notice_topCheck);

        Boolean isTop = false;
        switch (notice_topCheck) {
            case "true":
                isTop=true;
                break;
            case "false":
                isTop=false;
                break;
        }
        log.info("isTop = {}", isTop);
        Notice notice = infoPageService.saveNewNotice(
                multipartFile, isTop,
                notice_title, notice_content);

        return notice.getNotice_id();
    }

    @GetMapping("/info/notice/detail/{noticeId}")
    public String boardDetail(@PathVariable Long noticeId, @CurrentUser Account account,
                              HttpServletRequest request, HttpServletResponse response,
                              Model model) {

        model.addAttribute("account", account);

        Optional<Notice> currentNotice = noticeRepository.findById(noticeId);

        model.addAttribute("notice", currentNotice.get());

        model.addAttribute(new ReplyForm());

        return "info/info-notice-detail";
    }

    @ResponseBody
    @RequestMapping(value = "/info/notice/{id}/update", method = RequestMethod.POST)
    public ResponseEntity boardUpdate(@PathVariable Long id,
                                      @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile,
                                      @RequestParam(value = "notice_topCheck", required = false) String notice_topCheck,
                                      @RequestParam(value = "notice_title", required = false) String notice_title,
                                      @RequestParam(value = "notice_content", required = false) String notice_content) {
        Boolean isTop = false;
        switch (notice_topCheck) {
            case "true":
                isTop=true;
                break;
            case "false":
                isTop=false;
                break;
        }

        Optional<Notice> byId = noticeRepository.findById(id);
        Notice notice = byId.get();

        infoPageService.updateNotice(notice, multipartFile,
                isTop, notice_title, notice_content);

        return ResponseEntity.ok().build();
    }

    // 게시물 삭제
    @GetMapping("/info/notice/detail/{noticeId}/delete")
    public String boardDelete(@PathVariable long noticeId, @CurrentUser Account account, RedirectAttributes redirectAttributes) {
        Optional<Notice> currentNotice = noticeRepository.findById(noticeId);
        Notice notice = currentNotice.get();
        if (!account.getAccountType().equals(AccountType.ROLE_ADMIN)) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시물 삭제 권한이 없습니다.");
            return "redirect:/info/notice/detail/{noticeId}";
        }
        infoPageService.deleteNotice(notice);
        redirectAttributes.addFlashAttribute("deleteMessage", "해당 게시글이 삭제되었습니다.");
        return "redirect:/info/notice";
    }

    /* 공지사항 끝 */

    /* 건의사항 시작 */
    @GetMapping("/info/contact")
    public String contactPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "info/info-contact";
    }

    @ResponseBody
    @RequestMapping(value = "/inquire-new", method = RequestMethod.POST)
    public Long noticeFormSubmit(@CurrentUser Account account,
                                 @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile,
                                 @RequestParam(value = "contact_title", required = false) String contact_title,
                                 @RequestParam(value = "contact_content", required = false) String contact_content) {
        Inquire newInquire = infoPageService.saveNewInquire(
                multipartFile, account,
                contact_title, contact_content);

        return newInquire.getId();
    }

    @GetMapping("/info/contact/detail/{inquireId}")
    public String inquireDetail(@PathVariable Long inquireId, @CurrentUser Account account,
                              HttpServletRequest request, HttpServletResponse response,
                              Model model) {

        Optional<Inquire> inquire = inquireRepository.findById(inquireId);
        if (!inquire.get().getAccount().getNickname().equals(account.getNickname()) && !account.getAccountType().equals(AccountType.ROLE_ADMIN)) {
            return "redirect:/info/contact";
        }

        Optional<InquireAnswer> currentAnswer = inquireAnswerRepository.findByInquire(inquire.get());

        model.addAttribute("account", account);
        model.addAttribute("inquire", inquire.get());
        model.addAttribute("answer", currentAnswer);
        model.addAttribute(new InquireAnswerForm());

        return "info/info-contact-detail";
    }

    @GetMapping("/info/contact/lists/{type}")
    public String inquireList(@CurrentUser Account account, Model model, @PathVariable String type) {

        switch (type) {
            case "waiting" :
                List<Inquire> isAnsweredFalse = inquireRepository.findByIsAnsweredAndAccountOrderByUploadTimeDesc(false, account);
                model.addAttribute("inquires", isAnsweredFalse);
                break;
            case "replied" :
                List<Inquire> isAnsweredTrue = inquireRepository.findByIsAnsweredAndAccountOrderByUploadTimeDesc(true, account);
                model.addAttribute("inquires", isAnsweredTrue);
                break;
        }

        model.addAttribute("account", account);

        return "info/info-contact-lists";
    }

    @PostMapping("/manager/contact/detail/{id}/answered")
    public String inquireAnswer(@CurrentUser Account account, @PathVariable Long id, InquireAnswerForm inquireAnswerForm) {
        Optional<Inquire> currentInquire = inquireRepository.findById(id);
        infoPageService.inquireAnswerUpdate(currentInquire.get(), account, inquireAnswerForm);
        return "redirect:/info/contact/detail/{id}";
    }

    @ResponseBody
    @RequestMapping(value = "/inquire/image/delete", method = RequestMethod.POST)
    public ResponseEntity graduationDeleteImage(@RequestParam(value = "imageName") String imageName) {
        S3 s3 = s3Repository.findByImageName(imageName);

        infoPageService.deleteImage(s3);

        if(s3 != null) ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }

    /* 건의사항 끝 */
    @GetMapping("/info/faq")
    public String faqPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "info/info-faq";
    }
}
