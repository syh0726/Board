package com.example.board.responseDto.comment;

import com.example.board.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
@Getter
@ToString
public class CommentListResponseDto {
    private final List<CommentListItem> commentListItems;
    private final int commentsNum;

    @Builder
    public CommentListResponseDto(List<CommentListItem> commentListItems, int commentsNum) {
        this.commentListItems = commentListItems;
        this.commentsNum = commentsNum;
    }
}
