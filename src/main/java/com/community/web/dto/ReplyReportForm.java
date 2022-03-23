package com.community.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class ReplyReportForm {
    // 신고 내용
    @NotBlank(message = "신고 내용을 입력해주세요.")
    @Length(min = 2, max = 300)
    private String report_content;

    private String report_title;
}
