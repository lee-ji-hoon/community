package com.community.study.validator;

import com.community.study.form.MeetingsForm;
import com.community.study.form.StudyCalendarForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class MeetingFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MeetingsForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
