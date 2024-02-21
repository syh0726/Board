package com.board.repository.auth;

import com.board.domain.member.Member;
import com.board.domain.auth.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session,Long>,SessionRepositoryCustom {
    Optional<Session> findByMember(Member member);
}
