package com.community.study.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class StudyForm {

    @NotBlank
    @Length(min = 2, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{2,20}$")
    private String path;

    @NotBlank
    @Length(max = 50)
    private String title;

    @NotBlank
    @Length(max = 100)
    private String shortDescription;

    @NotBlank
    private String fullDescription;

    @NotBlank
    @Email
    private String managerEmail;

    private String studyMethod;

    private String studyPlaces;

    private String studyType;

    private int limitMember;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate limitMemberDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startStudyDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate limitStudyDate;



}
