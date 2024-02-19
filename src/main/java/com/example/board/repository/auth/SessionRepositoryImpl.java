package com.example.board.repository.auth;

import com.example.board.domain.auth.QSession;
import com.example.board.domain.auth.Session;
import com.example.board.domain.member.Member;
import com.example.board.domain.member.QMember;
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
