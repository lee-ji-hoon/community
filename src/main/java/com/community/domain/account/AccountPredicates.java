package com.community.domain.account;

import com.community.domain.tag.Tag;
import com.querydsl.core.types.Predicate;

import java.util.Set;

public class AccountPredicates {

    public static Predicate findByTags(Set<Tag> tags) {
        QAccount qAccount = QAccount.account;
        return qAccount.tags.any().in(tags);


    }
}
