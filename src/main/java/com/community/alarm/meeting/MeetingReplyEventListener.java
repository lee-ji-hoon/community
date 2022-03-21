package com.community.alarm.meeting;


import com.community.account.entity.Account;
import com.community.alarm.Alarm;
import com.community.alarm.AlarmRepository;
import com.community.alarm.AlarmType;
import com.community.config.AppProperties;
import com.community.study.entity.Meetings;
import com.community.study.entity.Study;
import com.community.study.repository.MeetingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@Async
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MeetingReplyEventListener {

    private final MeetingsRepository meetingsRepository;
    private final AlarmRepository alarmRepository;

    @EventListener
    public void meetingReplyCreate(MeetingReplyCreatePublish meetingReplyCreatePublish) {
        log.info("모임 댓글 실행");

        Meetings meetings = meetingReplyCreatePublish.getMeetings();
        Meetings byMeetingsId = meetingsRepository.findByMeetingsId(meetings.getMeetingsId());
        Study study = byMeetingsId.getStudy();

        log.info("study : {}", study);

        Set<Account> managers = study.getManagers();

        for (Account manager : managers) {
            log.info("manager : {}", manager);
            if (manager.isReplyByMeetings()) {
                log.info("모임 댓글 발송 study : {}", meetings.getMeetingsId() );
                log.info("모임 댓글 발송 account : {}", manager.getNickname());
                sendWeb(meetings, manager, study);
            }

        }
    }

    private void sendWeb(Meetings meetings, Account account, Study study) {
        Alarm alarm = new Alarm();
        alarm.setTitle(meetings.getMeetingTitle());
        alarm.setLink("/study/" + study.getEncodePath() +"/meetings");
        alarm.setChecked(false);
        alarm.setCreateAlarmTime(LocalDateTime.now());
        alarm.setPath(study.getPath());
        alarm.setMessage(meetings.getMeetingPlaces());
        alarm.setAccount(account);
        alarm.setAlarmType(AlarmType.REPLY);
        alarmRepository.save(alarm);
    }

}
