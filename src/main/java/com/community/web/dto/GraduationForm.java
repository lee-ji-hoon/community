package com.community.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class GraduationForm {

    @NotBlank
    @Length(max = 50)
    private String title;

    private int graduationDate;

    @NotBlank
    private String description;

    private String path;

    private String teamName;

    private String teamMember;

    private String graduationType;
}
