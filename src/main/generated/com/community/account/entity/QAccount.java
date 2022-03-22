package com.community.account.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = -1114206379L;

    public static final QAccount account = new QAccount("account");

    public final StringPath bannerImage = createString("bannerImage");

    public final StringPath bio = createString("bio");

    public final StringPath email = createString("email");

    public final StringPath emailCheckToken = createString("emailCheckToken");

    public final DateTimePath<java.time.LocalDateTime> emailCheckTokenGeneratedAt = createDateTime("emailCheckTokenGeneratedAt", java.time.LocalDateTime.class);

    public final BooleanPath emailVerified = createBoolean("emailVerified");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> joinedAt = createDateTime("joinedAt", java.time.LocalDateTime.class);

    public final BooleanPath likesByPost = createBoolean("likesByPost");

    public final ListPath<com.community.like.Likes, com.community.like.QLikes> likesList = this.<com.community.like.Likes, com.community.like.QLikes>createList("likesList", com.community.like.Likes.class, com.community.like.QLikes.class, PathInits.DIRECT2);

    public final StringPath location = createString("location");

    public final ListPath<com.community.market.Market, com.community.market.QMarket> marketsList = this.<com.community.market.Market, com.community.market.QMarket>createList("marketsList", com.community.market.Market.class, com.community.market.QMarket.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath occupation = createString("occupation");

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final BooleanPath replyByMeetings = createBoolean("replyByMeetings");

    public final BooleanPath replyByPost = createBoolean("replyByPost");

    public final BooleanPath replyCreateByWeb = createBoolean("replyCreateByWeb");

    public final StringPath studentId = createString("studentId");

    public final BooleanPath studyCreatedByEmail = createBoolean("studyCreatedByEmail");

    public final BooleanPath studyCreatedByWeb = createBoolean("studyCreatedByWeb");

    public final SetPath<com.community.tag.Tag, com.community.tag.QTag> tags = this.<com.community.tag.Tag, com.community.tag.QTag>createSet("tags", com.community.tag.Tag.class, com.community.tag.QTag.class, PathInits.DIRECT2);

    public final StringPath url = createString("url");

    public final StringPath username = createString("username");

    public QAccount(String variable) {
        super(Account.class, forVariable(variable));
    }

    public QAccount(Path<? extends Account> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccount(PathMetadata metadata) {
        super(Account.class, metadata);
    }

}

