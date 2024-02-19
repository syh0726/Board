package com.example.board.controller;

import com.example.board.config.data.MemberSession;
import com.example.board.requestDto.post.EditPostDto;
import com.example.board.requestDto.post.GetPostsDto;
import com.example.board.requestDto.post.NewPostDto;
import com.example.board.requestServiceDto.Post.*;
import com.example.board.responseDto.Post.PostLikeResponseDto;
import com.example.board.responseDto.Post.PostResponseDto;
import com.example.board.responseDto.Post.PostsResponseDto;
import com.example.board.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //글 리스트
    @GetMapping(value = {"/","/{category}"})
    public PostsResponseDto postList(@PathVariable(name ="category",required = false)String category,
                                     @ModelAttribute GetPostsDto getPostsDto){

        GetPostsServiceDto getPostsServiceDto=GetPostsServiceDto.builder()
                .getPostsDto(getPostsDto)
                .category(category)
                .build();

        return postService.getList(getPostsServiceDto);
    }

    //글 작성
    @PostMapping("/posts/new")
    public void newPost(@RequestBody @Valid NewPostDto newPostDto
            , MemberSession memberSession){

        NewPostServiceDto newPostServiceDto = NewPostServiceDto.builder()
                .newPostDto(newPostDto)
                .id(memberSession.id)
                .build();

        postService.newPost(newPostServiceDto);
    }
    //특정 글 불러오기
    @GetMapping("/posts/{postId}")
    public PostResponseDto postGet(@PathVariable(name = "postId") long postId){
        return postService.getPost(postId);
    }

    //글 수정
    @PatchMapping("/posts/{postId}")
    public void postEdit(@PathVariable(name="postId") long postId,
                         @RequestBody @Valid EditPostDto editPostDto
                        ,MemberSession memberSession){

        EditPostServiceDto editPostServiceDto=EditPostServiceDto.builder()
                .editPostDto(editPostDto)
                .postId(postId)
                .id(memberSession.id)
                .build();

        postService.editPost(editPostServiceDto);

    }

    //글 삭제
    @DeleteMapping("/posts/{postId}")
    public void postDelete(@PathVariable(name= "postId")long postId
            ,MemberSession memberSession){

        DeletePostServiceDto deletePostServiceDto=DeletePostServiceDto.builder()
                .postId(postId)
                .id(memberSession.id)
                .build();

        postService.deletePost(deletePostServiceDto);
    }

    //글 좋아요!!
    @PostMapping("/posts/{postId}/like")
    public PostLikeResponseDto postLikes(@PathVariable(name="postId")Long postId,
                                         MemberSession memberSession){

        LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
                .id(memberSession.id)
                .postId(postId)
                .isLike(true)
                .build();

        return postService.likesPost(likesPostServiceDto);
    }

    //글 싫어요!!
    @PostMapping("/posts/{postId}/dislike")
    public PostLikeResponseDto postDislikes(@PathVariable(name="postId")Long postId,
                                            MemberSession memberSession){

        LikesPostServiceDto likesPostServiceDto=LikesPostServiceDto.builder()
                .id(memberSession.id)
                .postId(postId)
                .isLike(false)
                .build();

        return postService.likesPost(likesPostServiceDto);
    }
}
