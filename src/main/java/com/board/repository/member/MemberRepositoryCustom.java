package com.board.repository.member;

import com.board.domain.member.Member;
import com.board.requestDto.member.FindPasswordDto;
import com.board.responseDto.member.GetActivictyResponseDto;

public interface MemberRepositoryCustom {
    Member findByEmail(String email);
    Member findByPhoneNumber(String phoneNumber);
    Member findByNickname(String nickname);
    Member getMemberbyId(long id);
    Member findBAccessToken(String accessToken);

    GetActivictyResponseDto getActivityPosts(long id);
    boolean findByEmailAndPhoneNumber(FindPasswordDto findPasswordDto);


}
