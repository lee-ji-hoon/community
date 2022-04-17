package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.graduation.Graduation;
import com.community.domain.graduation.GraduationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GraduationService {

    private final GraduationRepository graduationRepository;

    public Graduation createNewGraduation(Graduation graduation, Account account) {
        graduation.setAccount(account);
        graduation.setUploadTime(LocalDateTime.now());

        return graduationRepository.save(graduation);
    }
}
