package com.community.domain.alarm;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlarm is a Querydsl query type for Alarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarm extends EntityPathBase<Alarm> {

    private static final long serialVersionUID = -49381988L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlarm alarm = new QAlarm("alarm");

    public final NumberPath<Long> alarmId = createNumber("alarmId", Long.class);

    public final EnumPath<AlarmType> alarmType = createEnum("alarmType", AlarmType.class);

    public final BooleanPath checked = createBoolean("checked");

    public final DateTimePath<java.time.LocalDateTime> createAlarmTime = createDateTime("createAlarmTime", java.time.LocalDateTime.class);

    public final com.community.domain.account.QAccount fromAccount;

    public final StringPath link = createString("link");

    public final StringPath message = createString("message");

    public final StringPath path = createString("path");

    public final StringPath title = createString("title");

    public final com.community.domain.account.QAccount toAccount;

    public QAlarm(String variable) {
        this(Alarm.class, forVariable(variable), INITS);
    }

    public QAlarm(Path<? extends Alarm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlarm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlarm(PathMetadata metadata, PathInits inits) {
        this(Alarm.class, metadata, inits);
    }

    public QAlarm(Class<? extends Alarm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromAccount = inits.isInitialized("fromAccount") ? new com.community.domain.account.QAccount(forProperty("fromAccount")) : null;
        this.toAccount = inits.isInitialized("toAccount") ? new com.community.domain.account.QAccount(forProperty("toAccount")) : null;
    }

}

