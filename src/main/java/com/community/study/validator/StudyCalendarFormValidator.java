package com.community.study.validator;

import com.community.study.form.StudyCalendarForm;
import com.community.study.form.StudyForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudyCalendarFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return StudyCalendarForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StudyCalendarForm studyForm = (StudyCalendarForm)target;

        if(!isNotValidLimitMemberDate(studyForm)){
            errors.rejectValue("limitMemberDate", "wrong.date", "인원 모집 종료 일시를 다시 확인해주세요.");
        }

        if (!isNotValidLimitStudyDate(studyForm)) {
            errors.rejectValue("limitStudyDate", "wrong.date", "스터디 종료일을 다시 확인해주세요");
        }

        if (!isNotValidStartStudyDate(studyForm)) {
            errors.rejectValue("startStudyDate", "wrong.date", "스터디 시작일을 다시 확인해주세요.");
        }

        if (studyForm.getLimitMember() < 2 && studyForm.getLimitMember() > 30) {
            errors.rejectValue("limitMember", "wrong.member", "인원은 2~30명 사이로 선택해주세요");
        }
    }

    private boolean isNotValidStartStudyDate(StudyCalendarForm studyForm) {
        log.info(String.valueOf(studyForm.getStartStudyDate().isBefore(studyForm.getLimitStudyDate())));
        return studyForm.getStartStudyDate().isBefore(studyForm.getLimitStudyDate());
    }

    private boolean isNotValidLimitStudyDate(StudyCalendarForm studyForm) {
        log.info(String.valueOf(studyForm.getLimitStudyDate().isAfter(studyForm.getStartStudyDate())));
        return studyForm.getLimitStudyDate().isAfter(studyForm.getStartStudyDate());
    }

    private boolean isNotValidLimitMemberDate(StudyCalendarForm studyForm) {
        log.info(String.valueOf(studyForm.getLimitMemberDate().isBefore(studyForm.getStartStudyDate())));
        return studyForm.getLimitMemberDate().isBefore(studyForm.getStartStudyDate());
    }
}
