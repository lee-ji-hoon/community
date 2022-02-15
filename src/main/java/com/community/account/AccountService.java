package com.community.account;

import com.community.account.form.SignUpForm;
import com.community.config.AppProperties;
import com.community.mail.EmailMessage;
import com.community.mail.EmailService;
import com.community.profile.form.Notifications;
import com.community.profile.form.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender javaMailSender;

    // 회원가입
    @Transactional
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
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
        context.setVariable("link", "/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        context.setVariable("username", newAccount.getUsername());
        context.setVariable("linkName", "이메일 인증");
        context.setVariable("message", "멀티커뮤니티 사용을 위해서는 아래 링크를 클릭해주세요.");
        context.setVariable("host", appProperties.getHost());

        String process = templateEngine.process("account/send-email-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("멀티커뮤니티, 회원 가입 인증")
                .message(process)
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

    // 이메일 또는 닉네임 로그인
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }

        if (account == null) {
            throw new UsernameNotFoundException(emailOrNickname);
        }

        return new UserAccount(account);
    }

    // 프로필 업데이트
    public void updateProfile(Account account, Profile profile) {
        account.setUrl(profile.getUrl());
        account.setBio(profile.getBio());
        account.setOccupation(profile.getOccupation());
        account.setLocation(profile.getLocation());
        account.setProfileImage(profile.getProfileImage());

        accountRepository.save(account);
    }

    // 비밀번호 업데이트
    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    // 알림설정 업데이트
    public void updateNotifications(Account account, Notifications notifications) {
        account.setStudyCreatedByWeb(notifications.isStudyCreatedByWeb());
        account.setStudyCreatedByEmail(notifications.isStudyCreatedByEmail());
        account.setStudyUpdatedByWeb(notifications.isStudyUpdatedByWeb());
        account.setStudyUpdatedByEmail(notifications.isStudyUpdatedByEmail());
        account.setStudyEnrollmentResultByEmail(notifications.isStudyEnrollmentResultByEmail());
        account.setStudyEnrollmentResultByWeb(notifications.isStudyEnrollmentResultByWeb());
        accountRepository.save(account);
    }

    // 닉네임 업데이트
    public void updateNickname(Account account, String nickname) {
        account.setNickname(nickname);
        accountRepository.save(account);
        login(account);
    }

    // 로그인 링크 보내기
    public void sendLoginLink(Account account) {
        account.generateEmailCheckToken();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(account.getEmail());
        simpleMailMessage.setSubject("멀티 커뮤니티, 로그인 링크");
        simpleMailMessage.setText("/email-login-view?token=" + account.getEmailCheckToken() +
                "&email=" + account.getEmail());
        javaMailSender.send(simpleMailMessage);
    }
}
