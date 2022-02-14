package com.community.board;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class BoardForm {
    @NotBlank(message = "제목을 입력해주세요.")
    @Length(min = 3, max = 20)
    private String title;

    private String content;

    @NotBlank
    private String writer;

    private String boardTitle;
}
