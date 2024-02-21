package com.board.requestServiceDto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ChangePasswordServiceDto {
    private final long id;
    private final String newPassWord;
    private final String originPassword;

    @Builder
    public ChangePasswordServiceDto(long id, String newPassWord, String originPassword) {
        this.id = id;
        this.newPassWord = newPassWord;
        this.originPassword = originPassword;
    }
}
