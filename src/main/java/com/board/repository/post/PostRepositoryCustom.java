package com.board.repository.post;

import com.board.domain.post.Post;
import com.board.requestServiceDto.Post.GetPostsServiceDto;
import com.board.responseDto.Post.PostsResponseDto;

public interface PostRepositoryCustom {
    PostsResponseDto getPostsResponse(GetPostsServiceDto getPostsServiceDto);
    int getLikesNum(Long postId);

    Post getPostById(Long postId);

}
