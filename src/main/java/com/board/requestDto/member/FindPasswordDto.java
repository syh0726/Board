package com.board.requestDto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FindPasswordDto {

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private final String email;


    @NotBlank(message = "전화번호를 입력하세요")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$",message = "올바른 형식의 번호가 아닙니다.")
    private final String phoneNumber;

    @Builder
    public FindPasswordDto(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber.replace("-","");
    }
}
