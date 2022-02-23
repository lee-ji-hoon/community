package com.community.board.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ReportForm {
    // 신고 내용
    @NotBlank(message = "신고 내용을 입력해주세요.")
    @Length(min = 2, max = 300)
    private String report_content;

}
