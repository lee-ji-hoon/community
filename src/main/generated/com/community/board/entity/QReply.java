package com.community.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReply is a Querydsl query type for Reply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReply extends EntityPathBase<Reply> {

    private static final long serialVersionUID = -1790957959L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReply reply = new QReply("reply");

    public final com.community.account.entity.QAccount account;

    public final QBoard board;

    public final StringPath content = createString("content");

    public final com.community.council.QCouncil council;

    public final BooleanPath isReported = createBoolean("isReported");

    public final com.community.market.QMarket market;

    public final com.community.study.entity.QMeetings meetings;

    public final NumberPath<Integer> reportCount = createNumber("reportCount", Integer.class);

    public final NumberPath<Long> rid = createNumber("rid", Long.class);

    public final DateTimePath<java.time.LocalDateTime> uploadTime = createDateTime("uploadTime", java.time.LocalDateTime.class);

    public QReply(String variable) {
        this(Reply.class, forVariable(variable), INITS);
    }

    public QReply(Path<? extends Reply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReply(PathMetadata metadata, PathInits inits) {
        this(Reply.class, metadata, inits);
    }

    public QReply(Class<? extends Reply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.community.account.entity.QAccount(forProperty("account")) : null;
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.council = inits.isInitialized("council") ? new com.community.council.QCouncil(forProperty("council"), inits.get("council")) : null;
        this.market = inits.isInitialized("market") ? new com.community.market.QMarket(forProperty("market"), inits.get("market")) : null;
        this.meetings = inits.isInitialized("meetings") ? new com.community.study.entity.QMeetings(forProperty("meetings"), inits.get("meetings")) : null;
    }

}

