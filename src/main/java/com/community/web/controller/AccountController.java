package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.tag.Tag;
import com.community.web.dto.SignUpForm;
import com.community.domain.account.AccountRepository;
import com.community.web.dto.validator.SignUpFormValidator;
import com.community.domain.board.BoardRepository;
import com.community.service.BoardService;
import com.community.domain.account.CurrentUser;
import com.community.service.LikeService;
import com.community.web.dto.ProfileForm;
import com.community.service.AccountService;
import com.community.domain.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final LikeService likeService;
    private final BoardService boardService;

    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;
    private final StudyRepository studyRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    // 회원가입 진입
    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    // 회원가입 요청
    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);
        return "redirect:/";
    }

    // 이메일 토큰 확인 페이지
    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);

        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return "account/checked-email";
        }

        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return "account/checked-email";
        }

        accountService.completeSignUp(account);
        model.addAttribute(account);
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return "account/checked-email";
    }

    // 인증메일 재발송 페이지
    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

    // 인증메일 재발송
    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentUser Account account, Model model) {
        if (!account.canSendConfirmEmail()) {
            model.addAttribute("error", "인증 이메일은 1시간에 한번만 전송할 수 있습니다.");
            model.addAttribute("email", account.getEmail());
            return "account/check-email";
        }

        accountService.sendSignUpConfirmEmail(account);
        return "redirect:/";
    }

    // 프로필 진입
    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, Model model, @CurrentUser Account account) {
        Account byAccount = accountService.getAccount(nickname);
        Account accountWithTagsById = accountRepository.findAccountWithTagsById(byAccount.getId());
        Set<Tag> tags = accountWithTagsById.getTags();
        for (Tag tag : tags) {
            log.info("tags = {}", tag);
        }

        model.addAttribute(new ProfileForm(account));
        if (nickname == null) {
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");
        }
        model.addAttribute(account);
        model.addAttribute("byAccount", byAccount);
        model.addAttribute("isOwner", byAccount.equals(account));
        model.addAttribute("accountWithTagsById", accountWithTagsById);
        return "account/profile";
    }

    // 프로필 배너 이미지 변경
    @PostMapping("/profile/{nickname}/update-banner")
    public String updateBanner(@CurrentUser Account account, @PathVariable String nickname,
                               RedirectAttributes redirectAttributes, String image) {
        accountService.updateProfileBanner(account, image);
        redirectAttributes.addFlashAttribute("message", "프로필을 수정했습니다.");
        log.info("image : {}",image);

        return "redirect:/profile/{nickname}";
    }

    // 이메일 로그인
    @GetMapping("/email-login")
    public String sendEmailLoginLinkView(@CurrentUser Account account, Model model) {
        return "account/email-login";
    }

    // 이메일 로그인 요청
    @PostMapping("/email-login")
    public String sendEmailLoginLink(String email, Model model, RedirectAttributes redirectAttributes) {

        Account account = accountRepository.findByEmail(email);

        if (account == null) {
            model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
            return "account/email-login";
        }

        if (!account.canSendConfirmEmail()) {
            model.addAttribute("error", "이메일 로그인은 1시간 뒤에 사용할 수 있습니다.");
            // return "account/email-login";
        }

        accountService.sendLoginLink(account);
        redirectAttributes.addFlashAttribute("message", "이메일 인증 메일이 발송됐습니다.");
        return "redirect:/email-login";
    }

    // 이메일 로그인 요청 후 페이지
    @GetMapping("/email-login-view")
    public String EmailLoginView(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        model.addAttribute(account);


        if (account == null || !account.isValidToken(token)) {
            model.addAttribute("error", "로그인 할 수 없습니다.");
            return "account/email-login-view";
        }

        accountService.login(account);
        return "account/email-login-view";

        // TODO
        // 자동 로그인이 아닌 이메일로 임시 비밀번호 발급하는건 어떤지 생각하기
    }
}
