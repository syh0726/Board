package com.board.requestServiceDto.comment;

import com.board.requestDto.comment.EditCommentDto;
import lombok.Builder;
import lombok.Getter;


@Getter
public class EditCommentServiceDto {
    private final EditCommentDto editCommentDto;
    private final Long commentId;
    private final Long id;

    @Builder
    public EditCommentServiceDto(EditCommentDto editCommentDto, Long commentId, Long id) {
        this.editCommentDto = editCommentDto;
        this.commentId = commentId;
        this.id = id;
    }
}
