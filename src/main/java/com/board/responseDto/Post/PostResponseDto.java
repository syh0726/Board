package com.board.responseDto.Post;


import com.board.responseDto.comment.CommentListResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostResponseDto {
    private final PostResponseData postResponseData;
    private final CommentListResponseDto commentListResponseDto;

    @Builder
    public PostResponseDto(PostResponseData postResponseData, CommentListResponseDto commentListResponseDto) {
        this.postResponseData = postResponseData;
        this.commentListResponseDto = commentListResponseDto;
    }
}
