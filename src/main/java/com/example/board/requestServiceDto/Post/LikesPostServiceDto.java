package com.example.board.requestServiceDto.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LikesPostServiceDto {
    private final Long id;
    private final Long postId;
    private final boolean isLike;

    @Builder
    public LikesPostServiceDto(Long id, Long postId, boolean isLike) {
        this.id = id;
        this.postId = postId;
        this.isLike = isLike;
    }
}


