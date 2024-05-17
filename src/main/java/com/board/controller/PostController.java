package com.board.controller;

import com.board.config.data.MemberSession;
import com.board.domain.image.PostImage;
import com.board.requestServiceDto.Post.*;
import com.board.responseDto.Post.PostResponseDto;
import com.board.responseDto.Post.PostsResponseDto;
import com.board.service.PostService;
import com.board.requestDto.post.EditPostDto;
import com.board.requestDto.post.GetPostsDto;
import com.board.requestDto.post.NewPostDto;
import com.board.responseDto.Post.PostLikeResponseDto;
import com.board.service.S3service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final S3service s3service;

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
    public void newPost(@RequestPart @Valid NewPostDto newPostDto
            , MemberSession memberSession
            , @RequestPart(value = "files",required = false) List<MultipartFile> files) throws IOException {

        NewPostServiceDto newPostServiceDto = NewPostServiceDto.builder()
                .newPostDto(newPostDto)
                .id(memberSession.id)
                .build();

        //s3에 저장하고 url 가져오기
        List<String> listUrl=new ArrayList<>();
        if(files!=null){
            listUrl=s3service.saveFile(files,"raw");
        }

        postService.newPost(newPostServiceDto,listUrl);
    }
    //특정 글 불러오기
    @GetMapping("/posts/{postId}")
    public PostResponseDto postGet(@PathVariable(name = "postId") long postId){
        return postService.getPost(postId);
    }

    //글 수정
    @PostMapping("/posts/{postId}")
    public void postEdit(@PathVariable(name="postId") long postId,
                         @RequestPart @Valid EditPostDto editPostDto
                        ,@RequestPart(value = "deleteFiles",required = false)List<String> deleteFiles
                        ,@RequestPart(value = "files",required = false) List<MultipartFile> files
                        ,MemberSession memberSession) throws IOException {

        List<String> upLoadListUrl=new ArrayList<>();
        if(files!=null){
            upLoadListUrl=s3service.saveFile(files,"raw");
        }

        if(deleteFiles!=null){
            s3service.deleteFiles(deleteFiles,"raw");
        }

        EditPostServiceDto editPostServiceDto=EditPostServiceDto.builder()
                .editPostDto(editPostDto)
                .postId(postId)
                .id(memberSession.id)
                .build();

        postService.editPost(editPostServiceDto,upLoadListUrl);
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
