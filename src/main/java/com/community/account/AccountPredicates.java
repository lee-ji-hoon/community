package com.community.account;

import com.community.account.entity.QAccount;
import com.community.tag.Tag;
import com.querydsl.core.types.Predicate;

import java.util.Set;

public class AccountPredicates {

    public static Predicate findByTags(Set<Tag> tags) {
        QAccount qAccount = QAccount.account;
        return qAccount.tags.any().in(tags);


    }
}
