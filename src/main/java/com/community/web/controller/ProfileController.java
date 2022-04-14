package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.BoardRepository;
import com.community.domain.market.MarketRepository;
import com.community.domain.study.StudyRepository;
import com.community.infra.aws.S3Service;
import com.community.service.*;
import com.community.web.dto.*;
import com.community.domain.tag.TagRepository;
import com.community.web.dto.validator.NicknameValidator;
import com.community.web.dto.validator.PasswordFormValidator;
import com.community.domain.tag.Tag;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    private static final String SETTINGS_PROFILE_VIEW_NAME = "profile/settings/profile-settings";
    private static final String SETTINGS_PROFILE_URL = "/profile/settings/profile-settings";

    private static final String SETTINGS_PROFILE_IMG_VIEW_NAME = "profile/settings/profile-img";
    private static final String SETTINGS_PROFILE_IMG_URL = "/profile/settings/profile-img";

    private static final String SETTINGS_PASSWORD_VIEW_NAME = "profile/settings/password";
    private static final String SETTINGS_PASSWORD_URL = "/profile/settings/password";

    private static final String SETTINGS_ALARM_VIEW_NAME = "profile/settings/alarm";
    private static final String SETTINGS_ALARM_URL = "/profile/settings/alarm";

    private static final String SETTINGS_ACCOUNT_VIEW_NAME = "profile/settings/account";
    private static final String SETTINGS_ACCOUNT_URL = "/profile/settings/account";

    private static final String SETTINGS_WITHDRAW_VIEW_NAME = "profile/settings/withdraw";
    private static final String SETTINGS_WITHDRAW_URL = "/profile/settings/withdraw";

    private static final String SETTINGS_TAGS_VIEW_NAME = "profile/settings/tags";
    private static final String SETTINGS_TAGS_URL = "/profile/settings/tags";

    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final StudyRepository studyRepository;
    private final BoardRepository boardRepository;
    private final MarketRepository marketRepository;

    private final TagService tagService;
    private final NicknameValidator nicknameValidator;
    private final ObjectMapper objectMapper;
    private final S3Service s3Service;
    private final AccountService accountService;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("accountForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
    }

    // 프로필 진입
    @GetMapping("/profile/{nickname}/{division}")
    public String viewProfile(@PathVariable String nickname, @PathVariable String division,
                              @PageableDefault(size = 7, page = 0, sort = "uploadTime",
                                      direction = Sort.Direction.ASC) Pageable pageable,
                              @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                              Model model, @CurrentUser Account account) {
        if (nickname == null) {
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");
        }
        Account byAccount = accountService.getAccount(nickname);
        Account accountWithTagsById = accountRepository.findAccountWithTagsById(byAccount.getId());

        model.addAttribute(account);
        model.addAttribute("isOwner", byAccount.equals(account));
        model.addAttribute("byAccount", byAccount);

        switch (division) {
            case  "view" :
                model.addAttribute(new ProfileForm(byAccount));
                model.addAttribute("accountWithTagsById", accountWithTagsById);
                break;
            case "study" :
                model.addAttribute("enrolledStudyList",
                        studyRepository.findByMembersContainingOrderByPublishedDateTimeDesc(byAccount));
                model.addAttribute("myStudyList",
                        studyRepository.findByManagersContainingOrderByPublishedDateTimeDesc(byAccount));
                break;
            case "board" :
                return "redirect:/profile/"+byAccount.getNickname()+"/board/free" ;
            case "market" :
                return "redirect:/profile/"+byAccount.getNickname()+"/market/sell" ;
        }
        return "profile/view";
    }

    @GetMapping("/profile/{nickname}/{division}/{sortType}")
    public String viewProfileSortDivision(@PathVariable String nickname,
                                  @PathVariable String division,
                                  @PathVariable String sortType,
                                  Model model, @CurrentUser Account account,
                                  @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                          direction = Sort.Direction.ASC) Pageable pageable,
                                  @RequestParam(required = false, defaultValue = "0", value = "page") int page) {

        Account byAccount = accountService.getAccount(nickname);

        switch (division) {
            case "market" :
                model.addAttribute("myProductCountBySell",
                        marketRepository.countAllBySellerAndMarketType(account, "판매"));
                model.addAttribute("myProductCountByBuy",
                        marketRepository.countAllBySellerAndMarketType(account, "구매"));
                switch (sortType) {
                    case "sell":
                        model.addAttribute("market",
                                marketRepository.findBySellerAndMarketType(account, "판매", pageable));
                        break;
                    case "buy":
                        model.addAttribute("market",
                                marketRepository.findBySellerAndMarketType(account, "구매", pageable));
                        break;
                }
            case "board" :
                model.addAttribute("myBoardCountByFree",
                        boardRepository.countAllByWriterAndBoardTitle(account, "자유"));
                model.addAttribute("myBoardCountByForum",
                        boardRepository.countAllByWriterAndBoardTitle(account, "정보"));
                model.addAttribute("myBoardCountByQnA",
                        boardRepository.countAllByWriterAndBoardTitle(account, "질문"));
                switch (sortType) {
                    case "free":
                        model.addAttribute("board",
                                boardRepository.findByWriterAndBoardTitleAndIsReported(account, "자유", false, pageable));
                        break;
                    case "forum":
                        model.addAttribute("board",
                                boardRepository.findByWriterAndBoardTitleAndIsReported(account, "정보", false, pageable));
                        break;
                    case "qna":
                        model.addAttribute("board",
                                boardRepository.findByWriterAndBoardTitleAndIsReported(account, "질문", false, pageable));
                        break;
                }
        }
        model.addAttribute(account);
        model.addAttribute("isOwner", byAccount.equals(account));
        model.addAttribute("byAccount", byAccount);
        model.addAttribute("sortType", sortType);
        model.addAttribute("pageNo", page);

        return "profile/view";
    }

    // 프로필 변경 페이지
    @GetMapping(SETTINGS_PROFILE_URL)
    public String updateProfileForm(@CurrentUser Account account, Model model) {

        model.addAttribute(account);
        model.addAttribute(new ProfileForm(account));

        return SETTINGS_PROFILE_VIEW_NAME;
    }

    // 프로필 변경 요청
    @PostMapping(SETTINGS_PROFILE_URL)
    public String updateProfile(@CurrentUser Account account, @Valid ProfileForm profile, Errors errors,
                                Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PROFILE_VIEW_NAME;
        }

        profileService.updateProfile(account, profile);
        attributes.addFlashAttribute("message", "프로필을 수정했습니다.");
        return "redirect:" + SETTINGS_PROFILE_URL;
    }

    // 프로필 이미지 변경
    @GetMapping(SETTINGS_PROFILE_IMG_URL)
    public String updateProfileImageForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new ProfileForm(account));

        return SETTINGS_PROFILE_IMG_VIEW_NAME;
    }

    // 프로필 이미지 변경 요청
    @PostMapping(SETTINGS_PROFILE_IMG_URL)
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

        String profileImageKey = s3Service.upload(file, folderPath);
        String profile = S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + folderPath + profileImageKey;

        log.info("profileImage : {}", profile);
        profileService.updateProfileImage(account, profile, profileImageKey, folderPath);
        redirectAttributes.addFlashAttribute("message", "프로필이미지를 수정했습니다.");

        return "redirect:" + SETTINGS_PROFILE_IMG_URL;
    }

    // 패스워드 변경 페이지
    @GetMapping(SETTINGS_PASSWORD_URL)
    public String updatePasswordForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTINGS_PASSWORD_VIEW_NAME;
    }

    // 패스워드 변경 요청
    @PostMapping(SETTINGS_PASSWORD_URL)
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors,
                                 Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PASSWORD_VIEW_NAME;
        }

        profileService.updatePassword(account, passwordForm.getNewPassword());
        attributes.addFlashAttribute("message", "패스워드를 변경했습니다.");
        return "redirect:" + SETTINGS_PASSWORD_URL;
    }

    // 알림 설정 변경 페이지
    @GetMapping(SETTINGS_ALARM_URL)
    public String updateNotificationsForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new AlarmForm(account));
        return SETTINGS_ALARM_VIEW_NAME;
    }

    // 알림 설정 변경 요청
    @GetMapping(SETTINGS_ALARM_URL + "/update")
    @ResponseBody
    public String updateNotifications(@CurrentUser Account account, RedirectAttributes attributes,
                                              @RequestParam(required = false, value = "studyCreatedByWeb") String studyCreatedByWeb,
                                              @RequestParam(required = false, value = "studyCreatedByEmail") String studyCreatedByEmail,
                                              @RequestParam(required = false, value = "replyByMeetings") String replyByMeetings,
                                              @RequestParam(required = false, value = "replyByMarket") String replyByMarket,
                                              @RequestParam(required = false, value = "replyByPost") String replyByPost,
                                              @RequestParam(required = false, value = "likes") String likes) {


        account.setStudyCreatedByWeb(studyCreatedByWeb != null);
        account.setStudyCreatedByEmail(studyCreatedByEmail != null);
        account.setReplyByMarket(replyByMarket != null);
        account.setReplyByPost(replyByMeetings != null);
        account.setReplyByMeetings(replyByPost != null);
        account.setLikesByPost(likes != null);

        accountRepository.save(account);

        return "<div class=\"bg-blue-500 border p-4 relative rounded-md\" uk-alert id=\"isUpdated\">\n" +
                "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                "        <i class=\"icon-feather-x\"></i>\n" +
                "    </button>\n" +
                "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                "    <p class=\"text-white text-opacity-75\">알림이 설정이 변경됐습니다.</p>\n" +
                "</div>";

    }

    // 닉네임 변경 페이지
    @GetMapping(SETTINGS_ACCOUNT_URL)
    public String updateAccountForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new AccountForm());
        return SETTINGS_ACCOUNT_VIEW_NAME;
    }

    // 닉네임 변경 요청
    @PostMapping(SETTINGS_ACCOUNT_URL)
    public String updateAccount(@CurrentUser Account account, @Valid AccountForm accountForm, Errors errors,
                                Model model, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_ACCOUNT_VIEW_NAME;
        }

        profileService.updateNickname(account, accountForm.getNickname());
        redirectAttributes.addFlashAttribute("message", "닉네임을 수정했습니다.");
        return "redirect:" + SETTINGS_ACCOUNT_URL;
    }

    // 회원 탈퇴 페이지
    @GetMapping(SETTINGS_WITHDRAW_URL)
    public String withdrawAccountForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new WithdrawForm());
        return SETTINGS_WITHDRAW_VIEW_NAME;
    }

    // 회원 탈퇴 요청
    @PostMapping(SETTINGS_WITHDRAW_URL)
    public String withdrawAccount(@CurrentUser Account account, WithdrawForm withdrawForm,
                                  Errors errors, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_WITHDRAW_VIEW_NAME;
        }
        profileService.withdraw(account, withdrawForm.getCheckPassword());
        if (accountRepository.findByNickname(account.getNickname()) != null) {
            model.addAttribute(account);
            redirectAttributes.addFlashAttribute("withdraw_message", "비밀번호를 다시 확인해주세요.");
            return "redirect:" + SETTINGS_WITHDRAW_URL;
        } else {
            SecurityContextHolder.clearContext();
            return "redirect:/";
        }
    }

    // 태그 페이지
    @GetMapping(SETTINGS_TAGS_URL)
    public String TagsForm(@CurrentUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        Set<Tag> tags = profileService.getTags(account);
        model.addAttribute("tags", tags.stream().map(Tag::getTitle).collect(Collectors.toList()));

        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("tagList", objectMapper.writeValueAsString(allTags));

        return SETTINGS_TAGS_VIEW_NAME;
    }

    // 태그 추가 요청
    @PostMapping(SETTINGS_TAGS_URL + "/add")
    @ResponseBody
    public ResponseEntity addTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        Tag tag = tagService.findOrAdd(tagForm.getTagTitle());

        profileService.addTag(account, tag);
        return ResponseEntity.ok().build();
    }

    // 태그 삭제 요청
    @PostMapping(SETTINGS_TAGS_URL + "/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title);
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        profileService.removeTag(account, tag);
        return ResponseEntity.ok().build();
    }
}
