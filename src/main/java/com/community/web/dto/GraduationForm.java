package com.community.web.dto;

import com.community.domain.graduation.GraduationType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Data
public class GraduationForm {

    @NotBlank
    @Length(max = 50)
    private String title;

    @NotBlank
    private String description;

    private String teamName;

    private String teamMember;

    @Enumerated(EnumType.STRING)
    private GraduationType graduationType;
}
