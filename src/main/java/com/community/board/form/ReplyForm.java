package com.community.board.form;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ReplyForm {

    @NotBlank
    private String content;
}
