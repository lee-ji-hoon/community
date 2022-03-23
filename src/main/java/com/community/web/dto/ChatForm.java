package com.community.web.dto;

import com.community.domain.account.Account;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class ChatForm {

    private Long sender;

    private Long receiver;

    private Long marketId;

    @NotBlank
    private String content;

}
