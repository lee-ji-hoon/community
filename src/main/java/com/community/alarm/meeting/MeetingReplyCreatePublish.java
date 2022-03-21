package com.community.alarm.meeting;

import com.community.study.entity.Meetings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class MeetingReplyCreatePublish {

    private final Meetings meetings;

    public MeetingReplyCreatePublish(Meetings newMeetings) {
        log.info("MeetingReplyCreatePublish : {}", newMeetings);
        this.meetings = newMeetings;
    }
}
