package com.board.requestDto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class ChangeNicknameDto {
    @NotBlank(message = "닉네임을 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,16}$",message = "영어 또는 숫자 또는 한글로 구성된 2~16자 닉네임을 생성하세요.")
    private String nickName;

    @Builder
    public ChangeNicknameDto(String nickName) {
        this.nickName = nickName;
    }
}
