package com.board.responseDto.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class PostResponseData {
    private final Long id;
    private final Long authorId;
    private final String title;
    private final String content;
    private final String category;
    private final String nickName;
    private final String createdAt;
    private final int viewCnt;
    private final int likeCnt;

    @Builder
    public PostResponseData(Long id, Long authorId, String title, String content, String category, String nickName, String createdAt, int viewCnt, int likeCnt) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.nickName = nickName;
        this.createdAt = createdAt;
        this.viewCnt = viewCnt;
        this.likeCnt = likeCnt;
    }
}
