package com.example.board.requestServiceDto.comment;

import com.example.board.config.data.MemberSession;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteCommentServiceDto {
    private final Long commentId;

    private final Long id;

    @Builder
    public DeleteCommentServiceDto(Long commentId, Long id) {
        this.commentId = commentId;
        this.id = id;
    }
}
