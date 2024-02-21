package com.board.responseDto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SignInResponseDto {
    private final Long id;
    private final String nickName;
    private final String role;

    @Builder
    public SignInResponseDto(Long id, String nickName, String role) {
        this.id = id;
        this.nickName = nickName;
        this.role = role;
    }
}
