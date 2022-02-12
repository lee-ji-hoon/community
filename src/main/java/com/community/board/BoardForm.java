package com.community.board;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class BoardForm {
    @NotBlank
    @Length(min = 3, max = 20)
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String writer;

    @NotBlank
    private String boardTitle;
}
