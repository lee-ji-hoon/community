package com.community.study.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MeetingsForm {

    @Length(max = 20)
    public String meetingTitle;

    private String meetingDescription;

    @Length(max = 20)
    private String meetingPlaces;

    @Length(max = 20)
    private String meetingMethod;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime meetingDateTime;
}
