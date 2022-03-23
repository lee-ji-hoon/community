package com.community.infra.alarm;

import com.community.domain.account.Account;
import com.community.domain.board.Reply;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ReplyCreatePublish {

    private final Reply reply;
    private final Account fromAccount;

    public ReplyCreatePublish(Reply newReply, Account account) {
        log.info("ReplyCreatePublish : {}", newReply);
        this.reply = newReply;
        this.fromAccount = account;
    }
}
