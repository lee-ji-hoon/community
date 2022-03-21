package com.community.alarm.meeting;

import com.community.board.entity.Reply;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ReplyCreatePublish {

    private final Reply reply;

    public ReplyCreatePublish(Reply newReply) {
        log.info("ReplyCreatePublish : {}", newReply);
        this.reply = newReply;
    }
}
