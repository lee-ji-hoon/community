package com.community.domain.report;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardReport is a Querydsl query type for BoardReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardReport extends EntityPathBase<BoardReport> {

    private static final long serialVersionUID = -2109070440L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardReport boardReport = new QBoardReport("boardReport");

    public final com.community.domain.account.QAccount account;

    public final com.community.domain.board.QBoard board;

    public final StringPath report_content = createString("report_content");

    public final NumberPath<Long> report_id = createNumber("report_id", Long.class);

    public final StringPath report_title = createString("report_title");

    public final DateTimePath<java.time.LocalDateTime> reportTime = createDateTime("reportTime", java.time.LocalDateTime.class);

    public QBoardReport(String variable) {
        this(BoardReport.class, forVariable(variable), INITS);
    }

    public QBoardReport(Path<? extends BoardReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardReport(PathMetadata metadata, PathInits inits) {
        this(BoardReport.class, metadata, inits);
    }

    public QBoardReport(Class<? extends BoardReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.community.domain.account.QAccount(forProperty("account")) : null;
        this.board = inits.isInitialized("board") ? new com.community.domain.board.QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

