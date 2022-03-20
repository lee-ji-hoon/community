package com.community.alarm.study;

import com.community.study.entity.Study;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
@Slf4j
public class StudyCreatedPublish{

    private final Study study;

    public StudyCreatedPublish(Study newStudy) {
        log.info("studyCreatePublish");
        newStudy.setRecentAlarmDateTime(LocalDateTime.now());
        this.study = newStudy;
    }
}
