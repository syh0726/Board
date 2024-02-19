package com.example.board.repository.post;

import com.example.board.domain.post.Post;
import com.example.board.requestServiceDto.Post.GetPostsServiceDto;
import com.example.board.responseDto.Post.PostsResponseDto;

import java.util.List;

public interface PostRepositoryCustom {
    PostsResponseDto getPostsResponse(GetPostsServiceDto getPostsServiceDto);
    int getLikesNum(Long postId);

    Post getPostById(Long postId);

}
