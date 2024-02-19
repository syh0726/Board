package com.example.board.requestServiceDto.comment;

import com.example.board.config.data.MemberSession;
import com.example.board.requestDto.comment.EditCommentDto;
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
