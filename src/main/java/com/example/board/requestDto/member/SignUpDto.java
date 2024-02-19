package com.example.board.requestDto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
public class SignUpDto {

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private final String email;

    @NotBlank(message = "닉네임을 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,16}$",message = "영어 또는 숫자 또는 한글로 구성된 2~16자 닉네임을 생성하세요.")
    private final String nickName;

    @NotBlank(message = "패스워드를 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9!@#]{8,15}$",message = "대문자,소문자,숫자,특수기호(!@#)를 사용하여 8~15자리 비밀번호를 생성하세요.")
    private final String password;

    @NotBlank(message = "전화번호를 입력하세요")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$",message = "올바른 형식의 번호가 아닙니다.")
    private final String phoneNumber;

    @Builder
    public SignUpDto(String email, String nickName, String password, String phoneNumber) {
        this.email = email;
        this.nickName = nickName;
        this.password = password.replace("-","");
        this.phoneNumber = phoneNumber;
    }
}
