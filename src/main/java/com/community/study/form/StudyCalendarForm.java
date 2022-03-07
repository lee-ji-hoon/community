package com.community.study.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class StudyCalendarForm {

    private int limitMember;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate limitMemberDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startStudyDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate limitStudyDate;

}
