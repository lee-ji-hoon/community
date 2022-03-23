package com.community.infra.alarm;

import com.community.domain.account.Account;
import com.community.domain.study.Study;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Slf4j
public class StudyCreatedPublish{

    private final Study study;
    private final Account fromAccount;

    public StudyCreatedPublish(Study newStudy, Account Account) {
        log.info("studyCreatePublish");
        newStudy.setRecentAlarmDateTime(LocalDateTime.now());
        this.study = newStudy;
        this.fromAccount = Account;
    }
}
