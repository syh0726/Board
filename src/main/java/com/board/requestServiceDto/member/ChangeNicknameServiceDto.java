package com.board.requestServiceDto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChangeNicknameServiceDto {
    private final long id;
    private final String nickName;

    @Builder
    public ChangeNicknameServiceDto(long id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }
}
