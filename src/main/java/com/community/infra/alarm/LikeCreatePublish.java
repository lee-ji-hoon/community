package com.community.infra.alarm;

import com.community.domain.account.Account;
import com.community.domain.likes.Likes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class LikeCreatePublish {

    private final Likes likes;
    private final Account fromAccount;

    public LikeCreatePublish(Likes newLikes, Account account) {
        log.info("LikeCreatePublish : {}", newLikes);
        this.likes = newLikes;
        this.fromAccount = account;
    }
}
