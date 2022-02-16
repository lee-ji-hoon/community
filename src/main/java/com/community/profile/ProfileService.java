package com.community.profile;

import com.community.account.entity.Account;
import com.community.account.AccountRepository;
import com.community.account.AccountService;
import com.community.profile.form.Notifications;
import com.community.profile.form.ProfileForm;
import com.community.tag.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    // 프로필 업데이트
    public void updateProfile(Account account, ProfileForm profile) {
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
        accountService.login(account);
    }

    // 회원탈퇴
    public void withdraw(Account account, String checkPassword) throws Exception {
        Account deleteNickname = accountRepository.findByNickname(account.getNickname());
         if(deleteNickname.matchPassword(passwordEncoder, checkPassword)) {
             accountRepository.delete(deleteNickname);
        }
    }

    // 태그 추가
    public void addTag(Account account, Tag tag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getTags().add(tag));
    }

    // 태그 가져오기
    public Set<Tag> getTags(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getTags();
    }

    // 태그 지우기
    public void removeTag(Account account, Tag tag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getTags().remove(tag));
    }
}
