package com.board.requestServiceDto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChangePhoneServiceDto {
    private final long id;
    private final String phoneNumber;

    @Builder
    public ChangePhoneServiceDto(long id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }
}
