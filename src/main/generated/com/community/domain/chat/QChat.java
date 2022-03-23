package com.community.domain.chat;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChat is a Querydsl query type for Chat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChat extends EntityPathBase<Chat> {

    private static final long serialVersionUID = 1878328214L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChat chat = new QChat("chat");

    public final NumberPath<Long> chat_id = createNumber("chat_id", Long.class);

    public final StringPath content = createString("content");

    public final com.community.domain.market.QMarket market_id;

    public final BooleanPath readChk = createBoolean("readChk");

    public final DateTimePath<java.time.LocalDateTime> readTime = createDateTime("readTime", java.time.LocalDateTime.class);

    public final com.community.domain.account.QAccount receiver;

    public final NumberPath<Long> room = createNumber("room", Long.class);

    public final com.community.domain.account.QAccount sender;

    public final DateTimePath<java.time.LocalDateTime> sendTime = createDateTime("sendTime", java.time.LocalDateTime.class);

    public QChat(String variable) {
        this(Chat.class, forVariable(variable), INITS);
    }

    public QChat(Path<? extends Chat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChat(PathMetadata metadata, PathInits inits) {
        this(Chat.class, metadata, inits);
    }

    public QChat(Class<? extends Chat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.market_id = inits.isInitialized("market_id") ? new com.community.domain.market.QMarket(forProperty("market_id"), inits.get("market_id")) : null;
        this.receiver = inits.isInitialized("receiver") ? new com.community.domain.account.QAccount(forProperty("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new com.community.domain.account.QAccount(forProperty("sender")) : null;
    }

}

