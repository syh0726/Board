package com.example.board.repository.member;

import com.example.board.domain.member.Member;
import com.example.board.requestDto.member.FindPasswordDto;
import com.example.board.responseDto.member.GetActivictyResponseDto;

public interface MemberRepositoryCustom {
    Member findByEmail(String email);
    Member findByPhoneNumber(String phoneNumber);
    Member findByNickname(String nickname);
    Member getMemberbyId(long id);
    Member findBAccessToken(String accessToken);

    GetActivictyResponseDto getActivityPosts(long id);
    boolean findByEmailAndPhoneNumber(FindPasswordDto findPasswordDto);


}
