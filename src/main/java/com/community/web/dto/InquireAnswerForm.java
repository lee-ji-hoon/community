package com.community.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InquireAnswerForm {

    @NotBlank
    String content;

}
