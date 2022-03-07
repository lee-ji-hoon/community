package com.community.board.form;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class BoardForm {
    @NotBlank
    @Length(min = 5, max = 30)
    private String title;

    private String subTitle;

    @NotBlank
    private String content;

    private String boardTitle;

    private String subBoardTitle;
}
