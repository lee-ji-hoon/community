package com.community.profile;

import com.community.account.entity.Account;
import com.community.account.AccountRepository;
import com.community.account.CurrentUser;
import com.community.profile.form.*;
import com.community.profile.repository.TagRepository;
import com.community.profile.validator.NicknameValidator;
import com.community.profile.validator.PasswordFormValidator;
import com.community.profile.entity.Tag;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    private static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile-settings";
    private static final String SETTINGS_PROFILE_URL = "/settings/profile-settings";

    private static final String SETTINGS_PASSWORD_VIEW_NAME = "settings/password";
    private static final String SETTINGS_PASSWORD_URL = "/settings/password";

    private static final String SETTINGS_NOTIFICATIONS_VIEW_NAME = "settings/notifications";
    private static final String SETTINGS_NOTIFICATIONS_URL = "/settings/notifications";

    private static final String SETTINGS_ACCOUNT_VIEW_NAME = "settings/account";
    private static final String SETTINGS_ACCOUNT_URL = "/settings/account";

    private static final String SETTINGS_WITHDRAW_VIEW_NAME = "settings/withdraw";
    private static final String SETTINGS_WITHDRAW_URL = "/settings/withdraw";

    private static final String SETTINGS_TAGS_VIEW_NAME = "settings/tags";
    private static final String SETTINGS_TAGS_URL = "/settings/tags";

    private static final String SETTINGS_ZONES_VIEW_NAME = "settings/zones";
    private static final String SETTINGS_ZONES_URL = "/settings/zones";

    private final NicknameValidator nicknameValidator;
    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("accountForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
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
    @GetMapping(SETTINGS_NOTIFICATIONS_URL)
    public String updateNotificationsForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new NotificationsForm(account));
        return SETTINGS_NOTIFICATIONS_VIEW_NAME;
    }

    // 알림 설정 변경 요청
    @PostMapping(SETTINGS_NOTIFICATIONS_URL)
    public String updateNotifications(@CurrentUser Account account, @Valid NotificationsForm notifications, Errors errors,
                                      Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_NOTIFICATIONS_VIEW_NAME;
        }

        profileService.updateNotifications(account, notifications);
        attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
        return "redirect:" + SETTINGS_NOTIFICATIONS_URL;
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
    public String updateTags(@CurrentUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        Set<Tag> tags = profileService.getTags(account);
        model.addAttribute("tags", tags.stream().map(Tag::getTitle).collect(Collectors.toList()));

        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));

        return SETTINGS_TAGS_VIEW_NAME;
    }

    // 태그 추가 요청
    @PostMapping(SETTINGS_TAGS_URL + "/add")
    @ResponseBody
    public ResponseEntity addTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title);
        if (tag == null) {
            tag = tagRepository.save(Tag.builder().title(title).build());
        }

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

    @GetMapping(SETTINGS_ZONES_URL)
    public String updateZones(@CurrentUser Account account, Model model) {
        model.addAttribute(account);

        return SETTINGS_ZONES_VIEW_NAME;
    }



    /**
     * TODO
     * 활동 지역
     */

}
