package com.board.responseDto.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostLikeResponseDto {
    private final int likeCnt;

    @Builder
    public PostLikeResponseDto(int likeCnt) {
        this.likeCnt = likeCnt;
    }
}
