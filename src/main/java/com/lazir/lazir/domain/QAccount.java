package com.lazir.lazir.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;

import com.lazir.lazir.domain.Account.Account;
import com.lazir.lazir.domain.Account.Role;
import com.querydsl.core.types.Path;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = -1723386832L;

    public static final QAccount account = new QAccount("account");

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final DateTimePath<java.time.LocalDateTime> createTokenTime = createDateTime("createTokenTime", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final StringPath emailCheckToken = createString("emailCheckToken");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath location = createString("location");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final StringPath profileline = createString("profileline");

    public final StringPath provider = createString("provider");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public QAccount(String variable) {
        super(Account.class, forVariable(variable));
    }

    public QAccount(Path<? extends Account> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccount(PathMetadata metadata) {
        super(Account.class, metadata);
    }

}

