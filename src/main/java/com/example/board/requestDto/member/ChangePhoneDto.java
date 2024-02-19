package com.example.board.requestDto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ChangePhoneDto {
    @NotBlank(message = "전화번호를 입력하세요")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$",message = "올바른 형식의 번호가 아닙니다.")
    private String phoneNumber;

    @Builder
    public ChangePhoneDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
