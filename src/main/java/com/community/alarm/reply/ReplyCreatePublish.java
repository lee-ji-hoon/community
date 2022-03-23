package com.community.alarm.reply;

import com.community.account.entity.Account;
import com.community.board.entity.Reply;
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
