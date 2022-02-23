package com.community.board.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class BoardForm {
    @NotBlank(message = "제목을 입력해주세요.")
    @Length(min = 5, max = 30)
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String writer;

    private String boardTitle;
}
