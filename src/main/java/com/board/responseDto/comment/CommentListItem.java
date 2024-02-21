package com.board.responseDto.comment;

import com.board.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@ToString
@Getter
public class CommentListItem {
    private final Long id;
    private final String content;
    private final String nickName;
    private final String createdAt;

    @Builder
    public CommentListItem(Comment comment){
        this.id= comment.getId();
        this.content= comment.getContent();
        this.createdAt=comment.getCreteadAt().format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss"));
        this.nickName=comment.getMember().getNickName();
    }

}
