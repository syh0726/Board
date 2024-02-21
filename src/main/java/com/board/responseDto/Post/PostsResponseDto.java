package com.board.responseDto.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class PostsResponseDto {
    private final List<PostListItem> postListItems;
    private final int maxPage;
    @Builder
    public PostsResponseDto(List<PostListItem> postListItems, int maxPage) {
        this.postListItems = postListItems;
        this.maxPage = maxPage;
    }
}
