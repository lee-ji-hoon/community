package com.community.alarm.meeting;

import com.community.account.entity.Account;
import com.community.study.entity.Meetings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class MeetingCreatedPublish {

    private final Meetings meetings;
    private final Account fromAccount;

    public MeetingCreatedPublish(Meetings newMeeting, Account account) {
        log.info("MeetingCreatedPublish : {}", newMeeting);
        this.meetings = newMeeting;
        this.fromAccount = account;
    }
}
