package com.community.alarm.meeting;


import com.community.account.entity.Account;
import com.community.alarm.Alarm;
import com.community.alarm.AlarmRepository;
import com.community.alarm.AlarmType;
import com.community.board.entity.Reply;
import com.community.study.entity.Meetings;
import com.community.study.entity.Study;
import com.community.study.repository.MeetingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    public void meetingReplyCreate(ReplyCreatePublish replyCreatePublish) {
        log.info("모임 댓글 실행");

        Reply reply = replyCreatePublish.getReply();
        Meetings meetings = reply.getMeetings();
        Study study = meetings.getStudy();
        log.info("meetings : {}", meetings);

        Set<Account> managers = study.getManagers();

        for (Account manager : managers) {
            log.info("manager : {}", manager.getNickname());
            if (manager.isReplyByMeetings()) {
                log.info("모임 댓글 발송 study : {}", meetings.getMeetingsId() );
                log.info("모임 댓글 발송 account : {}", manager.getNickname());
                sendWeb(reply, meetings, manager, study);
            }

        }
    }

    private void sendWeb(Reply reply, Meetings meetings, Account account, Study study) {
        Alarm alarm = new Alarm();
        alarm.setTitle(meetings.getMeetingTitle());
        alarm.setLink("/study/" + study.getEncodePath() +"/meetings");
        alarm.setChecked(false);
        alarm.setCreateAlarmTime(LocalDateTime.now());
        alarm.setPath(study.getPath());
        alarm.setMessage(reply.getContent());
        alarm.setAccount(account);
        alarm.setAlarmType(AlarmType.MEETING_REPLY);
        alarmRepository.save(alarm);
    }

}
