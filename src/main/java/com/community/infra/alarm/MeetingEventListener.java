package com.community.infra.alarm;

import com.community.domain.account.Account;
import com.community.domain.alarm.Alarm;
import com.community.domain.alarm.AlarmRepository;
import com.community.domain.alarm.AlarmType;
import com.community.infra.config.AppProperties;
import com.community.infra.mail.EmailMessage;
import com.community.infra.mail.EmailService;
import com.community.domain.study.Meetings;
import com.community.domain.study.Study;
import com.community.domain.study.MeetingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Set;


@Component
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MeetingEventListener {

    private final MeetingsRepository meetingsRepository;
    private final EmailService emailService;
    private final AlarmRepository alarmRepository;

    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    @EventListener
    public void meetingCreate(MeetingCreatedPublish meetingCreatedPublish) {
        log.info("모임 알람 실행");

        Meetings meetings = meetingCreatedPublish.getMeetings();
        Meetings byMeetingsId = meetingsRepository.findByMeetingsId(meetings.getMeetingsId());
        Study study = byMeetingsId.getStudy();
        Account fromAccount = meetingCreatedPublish.getFromAccount();

        log.info("study : {}", study);

        Set<Account> members =study.getMembers();

        for (Account member : members) {
            log.info("member : {}", member);
            if (member.isReplyByMeetings()) {
                log.info("모임 이메일 발송 study : {}", study.getId() );
                log.info("모임 이메일 발송 account : {}", member.getEmail());
                sendEmail(meetings, member, study);
            }

            if (member.isStudyCreatedByWeb()) {
                log.info("모임 웹 발송 study : {}", study.getId() );
                log.info("모임 웹 발송 account : {}", member.getEmail());
                sendWeb(meetings, member, study, fromAccount);
            }
        }

    }
    private void sendWeb(Meetings meetings, Account toAccount, Study study, Account fromAccount) {
        Alarm alarm = new Alarm();
        alarm.setTitle(meetings.getMeetingTitle());
        alarm.setLink("/study/" + study.getEncodePath() +"/meetings");
        alarm.setChecked(false);
        alarm.setCreateAlarmTime(LocalDateTime.now());
        alarm.setPath(study.getPath());
        alarm.setMessage(meetings.getMeetingPlaces());
        alarm.setToAccount(toAccount);
        alarm.setFromAccount(fromAccount);
        alarm.setAlarmType(AlarmType.MEETING);
        toAccount.addAlarmSize();
        alarmRepository.save(alarm);
    }

    private void sendEmail(Meetings meetings, Account account, Study study) {
        Context context = new Context();
        context.setVariable("link", "/study/" + study.getEncodePath() + "/meetings");
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", meetings.getMeetingTitle());
        context.setVariable("message", "참여중인 스터디에 새로운 모임이 생성됐습니다.");
        context.setVariable("host", appProperties.getHost());

        String process = templateEngine.process("account/study-link", context);
        EmailMessage emailMessage = EmailMessage.builder()
                .subject(study.getTitle() + "의 새로운 모임이 생겼습니다.")
                .to(account.getEmail())
                .message(process)
                .build();

        emailService.sendEmail(emailMessage);
    }
}
