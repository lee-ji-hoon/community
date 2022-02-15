package com.community.profile;

import com.community.account.Account;
import com.community.account.AccountRepository;
import com.community.account.CurrentUser;
import com.community.profile.form.*;
import com.community.profile.validator.NicknameValidator;
import com.community.profile.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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

    private static final String SETTINGS_DELETE_ACCOUNT_URL = "/settings/accountDelete";

    private final NicknameValidator nicknameValidator;
    private final AccountRepository accountRepository;

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
        model.addAttribute(new Profile(account));

        return SETTINGS_PROFILE_VIEW_NAME;
    }

    // 프로필 변경 요청
    @PostMapping(SETTINGS_PROFILE_URL)
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile, Errors errors,
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
        model.addAttribute(new Notifications(account));
        return SETTINGS_NOTIFICATIONS_VIEW_NAME;
    }

    // 알림 설정 변경 요청
    @PostMapping(SETTINGS_NOTIFICATIONS_URL)
    public String updateNotifications(@CurrentUser Account account, @Valid Notifications notifications, Errors errors,
                                      Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_NOTIFICATIONS_VIEW_NAME;
        }

        profileService.updateNotifications(account, notifications);
        attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
        return "redirect:" + SETTINGS_NOTIFICATIONS_URL;
    }

    // 닉네임 변경 및 회원탈퇴 페이지
    @GetMapping(SETTINGS_ACCOUNT_URL)
    public String updateAccountForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new AccountForm());
        model.addAttribute(new WithdrawForm());
        return SETTINGS_ACCOUNT_VIEW_NAME;
    }

    // 닉네임 변경

    /**
     * TODO
     * 닉네임 변경이 정상적으로 작동하지만 같은 닉네임이나 유효하지 않은 닉네임의 경우 페이징 오류가뜸
     * 1. 회원 탈퇴를 따로 둔다
     * 2. 해결방법을 찾는다.
     */
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

    // 회원 탈퇴
    @PostMapping(SETTINGS_DELETE_ACCOUNT_URL)
    public String deleteAccount(@CurrentUser Account account, WithdrawForm withdrawForm,
                                Errors errors, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_ACCOUNT_VIEW_NAME;
        }

        profileService.withdraw(account, withdrawForm.getCheckPassword());
        if(accountRepository.findByNickname(account.getNickname()) != null){
            model.addAttribute(account);
            redirectAttributes.addFlashAttribute("withdraw_message", "비밀번호를 다시 확인해주세요.");
            return "redirect:" + SETTINGS_ACCOUNT_URL;
        }
        else {
            SecurityContextHolder.clearContext();
            return "redirect:/";
        }
    }

    /**
     * TODO
     * 관심 주제
     * 활동 지역
     */

}
