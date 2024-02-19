package com.example.board.repository.auth;

import com.example.board.domain.auth.Session;
import com.example.board.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session,Long>,SessionRepositoryCustom {
    Optional<Session> findByMember(Member member);
}
