package com.community.domain.account;

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

    private static final long serialVersionUID = -383407980L;

    public static final QAccount account = new QAccount("account");

    public final ListPath<com.community.domain.alarm.Alarm, com.community.domain.alarm.QAlarm> alarmList = this.<com.community.domain.alarm.Alarm, com.community.domain.alarm.QAlarm>createList("alarmList", com.community.domain.alarm.Alarm.class, com.community.domain.alarm.QAlarm.class, PathInits.DIRECT2);

    public final StringPath bannerImage = createString("bannerImage");

    public final StringPath bio = createString("bio");

    public final StringPath email = createString("email");

    public final StringPath emailCheckToken = createString("emailCheckToken");

    public final DateTimePath<java.time.LocalDateTime> emailCheckTokenGeneratedAt = createDateTime("emailCheckTokenGeneratedAt", java.time.LocalDateTime.class);

    public final BooleanPath emailVerified = createBoolean("emailVerified");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> joinedAt = createDateTime("joinedAt", java.time.LocalDateTime.class);

    public final BooleanPath likesByPost = createBoolean("likesByPost");

    public final ListPath<com.community.domain.likes.Likes, com.community.domain.likes.QLikes> likesList = this.<com.community.domain.likes.Likes, com.community.domain.likes.QLikes>createList("likesList", com.community.domain.likes.Likes.class, com.community.domain.likes.QLikes.class, PathInits.DIRECT2);

    public final StringPath location = createString("location");

    public final ListPath<com.community.domain.market.Market, com.community.domain.market.QMarket> marketsList = this.<com.community.domain.market.Market, com.community.domain.market.QMarket>createList("marketsList", com.community.domain.market.Market.class, com.community.domain.market.QMarket.class, PathInits.DIRECT2);

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

    public final SetPath<com.community.domain.tag.Tag, com.community.domain.tag.QTag> tags = this.<com.community.domain.tag.Tag, com.community.domain.tag.QTag>createSet("tags", com.community.domain.tag.Tag.class, com.community.domain.tag.QTag.class, PathInits.DIRECT2);

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

