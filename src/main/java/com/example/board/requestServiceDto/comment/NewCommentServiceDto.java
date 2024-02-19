package com.example.board.requestServiceDto.comment;

import com.example.board.config.data.MemberSession;
import com.example.board.requestDto.comment.NewCommentDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewCommentServiceDto {
    private final NewCommentDto newCommentDto;
    private final Long id;
    private final Long postId;

    @Builder
    public NewCommentServiceDto(NewCommentDto newCommentDto, Long id, Long postId) {
        this.newCommentDto = newCommentDto;
        this.id = id;
        this.postId = postId;
    }
}
