package com.board.repository.member;

import com.board.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom {
}
