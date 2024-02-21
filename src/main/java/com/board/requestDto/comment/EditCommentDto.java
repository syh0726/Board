package com.board.requestDto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class EditCommentDto {
    @NotBlank(message = "댓글을 입력하세요.")
    @Size(min = 1,max = 5000,message = "5000글자 까지만 입력 가능합니다.")
    private String content;

    @Builder
    public EditCommentDto(String content) {
        this.content = content;
    }
}
