package com.community.alarm.study;

import com.community.account.AccountPredicates;
import com.community.account.entity.Account;
import com.community.account.repository.AccountRepository;
import com.community.alarm.Alarm;
import com.community.alarm.AlarmRepository;
import com.community.alarm.AlarmType;
import com.community.config.AppProperties;
import com.community.mail.EmailMessage;
import com.community.mail.EmailService;
import com.community.study.entity.Study;
import com.community.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Component
@Async
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StudyEventListener {

    private final StudyRepository studyRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final AlarmRepository alarmRepository;

    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    @EventListener
    public void studyCreate(StudyCreatedPublish studyCreatedPublish) {
        log.info("스터디 알람 실행");
        Study study = studyRepository.findStudyWithTagsById(studyCreatedPublish.getStudy().getId());
        study.setRecentAlarmDateTime(LocalDateTime.now());

        Iterable<Account> accounts = accountRepository.findAll(AccountPredicates.findByTags(study.getTags()));

        log.info("tag가 동일한 account : {}", accounts);

        accounts.forEach( account -> {
            if (account.isStudyCreatedByEmail()) {
                log.info("스터디 이메일 발송 study : {}", study );
                log.info("스터디 이메일 발송 account : {}", account.getEmail() );
                sendEmail(study, account);
            }

            if (account.isStudyCreatedByWeb()) {
                sendWeb(study, account);
                // TODO 웹 view 구현 및 실제 발송 필요
                log.info("스터디 웹 발송 study : {}", study );
                log.info("스터디 웹 발송 account : {}", account );
            }
        });


    }

    private void sendWeb(Study study, Account account) {
        Alarm alarm = new Alarm();
        alarm.setTitle(study.getTitle());
        alarm.setLink("/study/" + study.getEncodePath());
        alarm.setChecked(false);
        alarm.setCreateAlarmTime(LocalDateTime.now());
        alarm.setMessage(study.getShortDescription());
        alarm.setAccount(account);
        alarm.setAlarmType(AlarmType.STUDY);
        alarmRepository.save(alarm);
    }

    private void sendEmail(Study study, Account account) {
        Context context = new Context();
        context.setVariable("link", "/study/" + study.getEncodePath());
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", study.getTitle());
        context.setVariable("message", "본인 관심에 해당하는 새로운 스터디가 생성됐습니다.");
        context.setVariable("host", appProperties.getHost());

        String process = templateEngine.process("account/study-link", context);
        EmailMessage emailMessage = EmailMessage.builder()
                .subject("unect의 " + study.getTitle() + "스터디가 생겼습 니다.")
                .to(account.getEmail())
                .message(process)
                .build();

        emailService.sendEmail(emailMessage);
    }
}

