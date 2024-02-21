package com.board.requestDto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class EditPostDto {
    @NotBlank(message = "제목을 입력하세요")
    private final String title;

    @NotBlank(message = "내용을 입력하세요")
    @Size(max = 10000,message = "최대 글자 수 입니다.")
    private final String content;

    @NotBlank(message = "카테고리를 선택하세요")
    private final String category;

    @Builder
    public EditPostDto(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
