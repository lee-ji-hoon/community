package com.community.domain.market;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMarket is a Querydsl query type for Market
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarket extends EntityPathBase<Market> {

    private static final long serialVersionUID = -1517833834L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarket market = new QMarket("market");

    public final StringPath itemDetail = createString("itemDetail");

    public final StringPath itemImg = createString("itemImg");

    public final StringPath itemName = createString("itemName");

    public final StringPath itemStatus = createString("itemStatus");

    public final DateTimePath<java.time.LocalDateTime> itemUploadTime = createDateTime("itemUploadTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> marketId = createNumber("marketId", Long.class);

    public final EnumPath<MarketItemStatus> marketItemStatus = createEnum("marketItemStatus", MarketItemStatus.class);

    public final StringPath marketType = createString("marketType");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final ListPath<com.community.domain.board.Reply, com.community.domain.board.QReply> replyList = this.<com.community.domain.board.Reply, com.community.domain.board.QReply>createList("replyList", com.community.domain.board.Reply.class, com.community.domain.board.QReply.class, PathInits.DIRECT2);

    public final com.community.domain.account.QAccount seller;

    public QMarket(String variable) {
        this(Market.class, forVariable(variable), INITS);
    }

    public QMarket(Path<? extends Market> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMarket(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMarket(PathMetadata metadata, PathInits inits) {
        this(Market.class, metadata, inits);
    }

    public QMarket(Class<? extends Market> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.seller = inits.isInitialized("seller") ? new com.community.domain.account.QAccount(forProperty("seller")) : null;
    }

}

