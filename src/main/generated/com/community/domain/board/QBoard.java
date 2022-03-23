package com.community.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -1832459514L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final NumberPath<Long> bid = createNumber("bid", Long.class);

    public final StringPath boardTitle = createString("boardTitle");

    public final StringPath content = createString("content");

    public final BooleanPath isReported = createBoolean("isReported");

    public final ListPath<com.community.domain.likes.Likes, com.community.domain.likes.QLikes> likesList = this.<com.community.domain.likes.Likes, com.community.domain.likes.QLikes>createList("likesList", com.community.domain.likes.Likes.class, com.community.domain.likes.QLikes.class, PathInits.DIRECT2);

    public final NumberPath<Integer> pageView = createNumber("pageView", Integer.class);

    public final ListPath<Reply, QReply> replyList = this.<Reply, QReply>createList("replyList", Reply.class, QReply.class, PathInits.DIRECT2);

    public final NumberPath<Integer> reportCount = createNumber("reportCount", Integer.class);

    public final StringPath subBoardTitle = createString("subBoardTitle");

    public final StringPath subTitle = createString("subTitle");

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updateTime = createDateTime("updateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> uploadTime = createDateTime("uploadTime", java.time.LocalDateTime.class);

    public final com.community.domain.account.QAccount writer;

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writer = inits.isInitialized("writer") ? new com.community.domain.account.QAccount(forProperty("writer")) : null;
    }

}

