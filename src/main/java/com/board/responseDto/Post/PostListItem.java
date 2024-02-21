package com.board.responseDto.Post;

import com.board.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
@Getter
@ToString
public class PostListItem {
    private final Long id;
    private final String title;
    private final String createdAt;
    private final String nickName;
    private final int viewCnt;
    private final int commentNum;
    private final int likeCnt;


    @Builder
    public PostListItem(Post post, int commentNum, int likeCnt) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss"));
        this.nickName = post.getMember().getNickName();
        this.viewCnt = post.getViewCnt();
        this.commentNum =commentNum ;
        this.likeCnt =likeCnt ;
    }
}
