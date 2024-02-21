package com.board.requestServiceDto.Post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeletePostServiceDto {
    private final Long postId;
    private final Long id;

    @Builder
    public DeletePostServiceDto(Long postId, Long id) {
        this.postId = postId;
        this.id = id;
    }
}
