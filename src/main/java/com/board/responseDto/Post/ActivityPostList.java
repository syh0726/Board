package com.board.responseDto.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@ToString
@Getter
public class ActivityPostList {
    private final long postId;
    private final String title;
    private final String createdAt;

    @Builder

    public ActivityPostList(long postId, String title, String createdAt) {
        this.postId = postId;
        this.title = title;
        this.createdAt = createdAt;
    }
}
