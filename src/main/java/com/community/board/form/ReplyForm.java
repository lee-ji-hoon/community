package com.community.board.form;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReplyForm {

    @NotBlank
    private String content;
}
