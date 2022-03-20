package com.community.alarm.meeting;

import com.community.study.entity.Meetings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class MeetingCreatedPublish {

    private final Meetings meetings;

    public MeetingCreatedPublish(Meetings newMeeting) {
        log.info("MeetingCreatedPublish : {}", newMeeting);
        this.meetings = newMeeting;
    }
}
