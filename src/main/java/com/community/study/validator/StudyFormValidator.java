package com.community.study.validator;

import com.community.study.StudyRepository;
import com.community.study.form.StudyForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
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

/*        if(isNotValidLimitMemberDate(studyForm)){
            errors.rejectValue("limitMemberDate", "wrong.path", "인원 모집 종료 일시를 다시 확인해주세요.");
        }*/

    }

/*    private boolean isNotValidLimitMemberDate(StudyForm studyForm) {
        return studyForm.getStartStudyDate().isBefore(studyForm.getLimitMemberDate());
    }*/
}
