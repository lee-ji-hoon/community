package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.account.AccountType;
import com.community.infra.config.AppProperties;
import com.community.web.dto.SignUpForm;
import com.community.domain.account.AccountRepository;
import com.community.domain.account.UserAccount;
import com.community.infra.mail.EmailMessage;
import com.community.infra.mail.EmailService;
import com.community.web.dto.ProfileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.validation.Valid;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

    // 회원가입
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        newAccount.setAccountType(AccountType.ROLE_USER);
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    // 회원가입 내용 저장
    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .studentId(signUpForm.getStudentId())
                .username(signUpForm.getUsername())
                .build();

        return accountRepository.save(account);
    }

    // 이메일 인증 메시지 보내기
    public void sendSignUpConfirmEmail(Account newAccount) {
        Context context = new Context();
        context.setVariable("link", "/check-email-token?token=" +
                newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        context.setVariable("username", newAccount.getUsername());
        context.setVariable("linkName", "이메일 인증");
        context.setVariable("message", "멀티커뮤니티 사용을 위해서는 아래 링크를 클릭해주세요.");
        context.setVariable("host", appProperties.getHost());

        String message = templateEngine.process("account/send-email-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("멀티커뮤니티, 회원 가입 인증")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }

    // 로그인 링크 보내기
    public void sendLoginLink(Account account) {
        Context context = new Context();
        context.setVariable("link", "/email-login-view?token=" +
                account.getEmailCheckToken() +
                "&email=" + account.getEmail());
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", "멀티커뮤티 로그인하기");
        context.setVariable("message", "로그인 하려면 아래 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());

        String message = templateEngine.process("account/send-email-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("멀티커뮤니티, 로그인 링크")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    // 로그인
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    // 회원가입 성공 -> 자동 로그인
    public void completeSignUp(Account account) {
        account.completeEmailCheck();
        login(account);
    }

    // 이메일 로그인
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);

        if (account == null) {
            throw new UsernameNotFoundException(emailOrNickname);
        }

        return new UserAccount(account);
    }

    public Account getAccount(String nickname) {
        Account byNickname = accountRepository.findByNickname(nickname);
        if (byNickname == null) {
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");
        }
        return byNickname;
    }
}

