package com.board.requestDto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@ToString
@Getter
public class NewCommentDto {
    @NotBlank(message = "댓글을 입력하세요.")
    @Size(min = 1,max = 1000,message = "1000글자 까지만 입력 가능합니다.")
    private String content;

    @Builder
    public NewCommentDto(String content) {
        this.content = content;
    }
}
