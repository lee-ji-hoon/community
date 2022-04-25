package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.Board;
import com.community.domain.inquire.Inquire;
import com.community.infra.aws.S3;
import com.community.infra.aws.S3Repository;
import com.community.infra.aws.S3Service;
import com.community.service.InfoPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InfoPageController {

    private final InfoPageService infoPageService;
    private final S3Repository s3Repository;
    private final S3Service s3Service;

    @GetMapping("/info/about")
    public String aboutPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "info/info-about";
    }

    /* 건의사항 시작 */
    @GetMapping("/info/contact")
    public String contactPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "info/info-contact";
    }

    @ResponseBody
    @RequestMapping(value = "/contact-new", method = RequestMethod.POST)
    public Long contactFormSubmit(@CurrentUser Account account,
                                     @RequestParam(value = "article_file") List<MultipartFile> multipartFile,
                                     @RequestParam(value = "contact_title", required = false) String contact_title,
                                     @RequestParam(value = "contact_content", required = false) String contact_content) {
        Inquire newInquire = infoPageService.saveNewInquire(
                multipartFile, account,
                contact_title, contact_content);

        return newInquire.getId();
    }

    @ResponseBody
    @RequestMapping(value = "/board/image/delete", method = RequestMethod.POST)
    public ResponseEntity graduationDeleteImage(@RequestParam(value = "imageName") String imageName) {
        S3 s3 = s3Repository.findByImageName(imageName);

        infoPageService.deleteImage(s3);

        if(s3 != null) ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }

    /* 건의사항 끝 */

    @GetMapping("/info/privacy")
    public String privacyPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "info/info-privacy";
    }
    @GetMapping("/info/faq")
    public String faqPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "info/info-faq";
    }
}
