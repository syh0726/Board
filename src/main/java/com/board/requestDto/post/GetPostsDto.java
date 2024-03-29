package com.board.requestDto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
public class GetPostsDto {
    private final Integer page;
    private final String condition;
    private final String keyword;

    @Builder
    public GetPostsDto(Integer page, String condition, String keyword) {
        this.page = Objects.requireNonNullElse(page, 1);
        this.condition = condition;
        this.keyword = keyword;
    }
}
