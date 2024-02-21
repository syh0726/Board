package com.board.repository.auth;

import com.board.domain.auth.QSession;
import com.board.domain.auth.Session;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SessionRepositoryImpl implements SessionRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Session findByAccessToken(String accessToken) {
        return jpaQueryFactory.selectFrom(QSession.session)
                .where(QSession.session.accessToken.eq(accessToken))
                .fetchOne();
    }
}
