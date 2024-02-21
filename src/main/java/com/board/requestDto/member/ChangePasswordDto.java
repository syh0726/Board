package com.board.requestDto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChangePasswordDto {
    @NotBlank(message = "패스워드를 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9!@#]{8,15}$",message = "대문자,소문자,숫자,특수기호(!@#)를 사용하여 8~15자리 비밀번호를 생성하세요.")
    private final String originPassword;

    @NotBlank(message = "패스워드를 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9!@#]{8,15}$",message = "대문자,소문자,숫자,특수기호(!@#)를 사용하여 8~15자리 비밀번호를 생성하세요.")
    private final String newPassword;

    @Builder
    public ChangePasswordDto(String originPassword, String newPassword) {
        this.originPassword = originPassword;
        this.newPassword = newPassword;
    }
}
