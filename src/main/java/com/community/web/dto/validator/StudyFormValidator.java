package com.community.web.dto.validator;

import com.community.domain.study.StudyRepository;
import com.community.web.dto.StudyForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudyFormValidator implements Validator {

    private final StudyRepository studyRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return StudyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StudyForm studyForm = (StudyForm)target;
        if (studyRepository.existsByPath(studyForm.getPath())) {
            errors.rejectValue("path", "wrong.path", "해당 스터디 경로를 사용할 수 없습니다 다시 작성해주세요.");
        }

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

    private boolean isNotValidStartStudyDate(StudyForm studyForm) {
        log.info(String.valueOf(studyForm.getStartStudyDate().isBefore(studyForm.getLimitStudyDate())));
        return studyForm.getStartStudyDate().isBefore(studyForm.getLimitStudyDate());
    }

    private boolean isNotValidLimitStudyDate(StudyForm studyForm) {
        log.info(String.valueOf(studyForm.getLimitStudyDate().isAfter(studyForm.getStartStudyDate())));
        return studyForm.getLimitStudyDate().isAfter(studyForm.getStartStudyDate());
    }

    private boolean isNotValidLimitMemberDate(StudyForm studyForm) {
        log.info(String.valueOf(studyForm.getLimitMemberDate().isBefore(studyForm.getStartStudyDate())));
        return studyForm.getLimitMemberDate().isBefore(studyForm.getStartStudyDate());
    }
}
