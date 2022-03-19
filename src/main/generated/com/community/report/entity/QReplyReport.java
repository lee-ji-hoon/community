package com.community.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReplyReport is a Querydsl query type for ReplyReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReplyReport extends EntityPathBase<ReplyReport> {

    private static final long serialVersionUID = 184057435L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReplyReport replyReport = new QReplyReport("replyReport");

    public final com.community.account.entity.QAccount account;

    public final com.community.board.entity.QReply reply;

    public final StringPath report_content = createString("report_content");

    public final NumberPath<Long> report_id = createNumber("report_id", Long.class);

    public final StringPath report_title = createString("report_title");

    public final DateTimePath<java.time.LocalDateTime> reportTime = createDateTime("reportTime", java.time.LocalDateTime.class);

    public QReplyReport(String variable) {
        this(ReplyReport.class, forVariable(variable), INITS);
    }

    public QReplyReport(Path<? extends ReplyReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReplyReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReplyReport(PathMetadata metadata, PathInits inits) {
        this(ReplyReport.class, metadata, inits);
    }

    public QReplyReport(Class<? extends ReplyReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.community.account.entity.QAccount(forProperty("account")) : null;
        this.reply = inits.isInitialized("reply") ? new com.community.board.entity.QReply(forProperty("reply"), inits.get("reply")) : null;
    }

}

