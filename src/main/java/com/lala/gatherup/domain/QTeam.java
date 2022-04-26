package com.lala.gatherup.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeam is a Querydsl query type for Team
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTeam extends EntityPathBase<Team> {

    private static final long serialVersionUID = 1251184442L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeam team = new QTeam("team");

    public final BooleanPath closed = createBoolean("closed");

    public final DateTimePath<java.time.LocalDateTime> closedTime = createDateTime("closedTime", java.time.LocalDateTime.class);

    public final StringPath context = createString("context");

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final QAccount manager;

    public final NumberPath<Integer> memberLimit = createNumber("memberLimit", Integer.class);

    public final ListPath<Account, QAccount> members = this.<Account, QAccount>createList("members", Account.class, QAccount.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final BooleanPath published = createBoolean("published");

    public final BooleanPath recruiting = createBoolean("recruiting");

    public final DateTimePath<java.time.LocalDateTime> recruitUpdateTime = createDateTime("recruitUpdateTime", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public final StringPath URL = createString("URL");

    public final ListPath<Account, QAccount> waitting = this.<Account, QAccount>createList("waitting", Account.class, QAccount.class, PathInits.DIRECT2);

    public QTeam(String variable) {
        this(Team.class, forVariable(variable), INITS);
    }

    public QTeam(Path<? extends Team> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeam(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeam(PathMetadata metadata, PathInits inits) {
        this(Team.class, metadata, inits);
    }

    public QTeam(Class<? extends Team> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.manager = inits.isInitialized("manager") ? new QAccount(forProperty("manager")) : null;
    }

}

