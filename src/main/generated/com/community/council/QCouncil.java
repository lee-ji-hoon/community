package com.community.council;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCouncil is a Querydsl query type for Council
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouncil extends EntityPathBase<Council> {

    private static final long serialVersionUID = 1524720130L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCouncil council = new QCouncil("council");

    public final DatePath<java.time.LocalDate> applyPeriodEndDate = createDate("applyPeriodEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> applyPeriodStartDate = createDate("applyPeriodStartDate", java.time.LocalDate.class);

    public final NumberPath<Long> cid = createNumber("cid", Long.class);

    public final StringPath contactNum = createString("contactNum");

    public final DatePath<java.time.LocalDate> eventEndDate = createDate("eventEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> eventStartDate = createDate("eventStartDate", java.time.LocalDate.class);

    public final NumberPath<Integer> pageView = createNumber("pageView", Integer.class);

    public final StringPath postContent = createString("postContent");

    public final StringPath postLink = createString("postLink");

    public final StringPath postSort = createString("postSort");

    public final StringPath postTarget = createString("postTarget");

    public final StringPath postTitle = createString("postTitle");

    public final com.community.account.entity.QAccount postWriter;

    public final DateTimePath<java.time.LocalDateTime> uploadTime = createDateTime("uploadTime", java.time.LocalDateTime.class);

    public QCouncil(String variable) {
        this(Council.class, forVariable(variable), INITS);
    }

    public QCouncil(Path<? extends Council> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCouncil(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCouncil(PathMetadata metadata, PathInits inits) {
        this(Council.class, metadata, inits);
    }

    public QCouncil(Class<? extends Council> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.postWriter = inits.isInitialized("postWriter") ? new com.community.account.entity.QAccount(forProperty("postWriter")) : null;
    }

}

