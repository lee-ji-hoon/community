package com.community.domain.study;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudy is a Querydsl query type for Study
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudy extends EntityPathBase<Study> {

    private static final long serialVersionUID = 2051222156L;

    public static final QStudy study = new QStudy("study");

    public final StringPath fullDescription = createString("fullDescription");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final NumberPath<Integer> limitMember = createNumber("limitMember", Integer.class);

    public final DatePath<java.time.LocalDate> limitMemberDate = createDate("limitMemberDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> limitStudyDate = createDate("limitStudyDate", java.time.LocalDate.class);

    public final StringPath managerEmail = createString("managerEmail");

    public final SetPath<com.community.domain.account.Account, com.community.domain.account.QAccount> managers = this.<com.community.domain.account.Account, com.community.domain.account.QAccount>createSet("managers", com.community.domain.account.Account.class, com.community.domain.account.QAccount.class, PathInits.DIRECT2);

    public final ListPath<Meetings, QMeetings> meetingsList = this.<Meetings, QMeetings>createList("meetingsList", Meetings.class, QMeetings.class, PathInits.DIRECT2);

    public final NumberPath<Integer> memberCount = createNumber("memberCount", Integer.class);

    public final SetPath<com.community.domain.account.Account, com.community.domain.account.QAccount> members = this.<com.community.domain.account.Account, com.community.domain.account.QAccount>createSet("members", com.community.domain.account.Account.class, com.community.domain.account.QAccount.class, PathInits.DIRECT2);

    public final StringPath path = createString("path");

    public final DateTimePath<java.time.LocalDateTime> publishedDateTime = createDateTime("publishedDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> recentAlarmDateTime = createDateTime("recentAlarmDateTime", java.time.LocalDateTime.class);

    public final StringPath shortDescription = createString("shortDescription");

    public final DatePath<java.time.LocalDate> startStudyDate = createDate("startStudyDate", java.time.LocalDate.class);

    public final StringPath studyMethod = createString("studyMethod");

    public final StringPath studyPlaces = createString("studyPlaces");

    public final SetPath<com.community.domain.tag.Tag, com.community.domain.tag.QTag> tags = this.<com.community.domain.tag.Tag, com.community.domain.tag.QTag>createSet("tags", com.community.domain.tag.Tag.class, com.community.domain.tag.QTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public QStudy(String variable) {
        super(Study.class, forVariable(variable));
    }

    public QStudy(Path<? extends Study> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStudy(PathMetadata metadata) {
        super(Study.class, metadata);
    }

}

