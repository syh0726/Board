package com.example.board.responseDto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class GetProfileResponseDto {
    private final String email;
    private final String phoneNumber;
    private final String nickName;


    @Builder
    public GetProfileResponseDto(String email, String phoneNumber, String nickName) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
    }
}
