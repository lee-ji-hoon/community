package com.community.study.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeetings is a Querydsl query type for Meetings
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeetings extends EntityPathBase<Meetings> {

    private static final long serialVersionUID = -1508703092L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeetings meetings = new QMeetings("meetings");

    public final StringPath meetingDescription = createString("meetingDescription");

    public final StringPath meetingMethod = createString("meetingMethod");

    public final StringPath meetingPlaces = createString("meetingPlaces");

    public final NumberPath<Long> meetingsId = createNumber("meetingsId", Long.class);

    public final StringPath meetingTitle = createString("meetingTitle");

    public final ListPath<com.community.board.entity.Reply, com.community.board.entity.QReply> replyList = this.<com.community.board.entity.Reply, com.community.board.entity.QReply>createList("replyList", com.community.board.entity.Reply.class, com.community.board.entity.QReply.class, PathInits.DIRECT2);

    public final QStudy study;

    public final DateTimePath<java.time.LocalDateTime> uploadTime = createDateTime("uploadTime", java.time.LocalDateTime.class);

    public final com.community.account.entity.QAccount writer;

    public QMeetings(String variable) {
        this(Meetings.class, forVariable(variable), INITS);
    }

    public QMeetings(Path<? extends Meetings> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeetings(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeetings(PathMetadata metadata, PathInits inits) {
        this(Meetings.class, metadata, inits);
    }

    public QMeetings(Class<? extends Meetings> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.study = inits.isInitialized("study") ? new QStudy(forProperty("study")) : null;
        this.writer = inits.isInitialized("writer") ? new com.community.account.entity.QAccount(forProperty("writer")) : null;
    }

}

