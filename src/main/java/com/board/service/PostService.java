package com.board.service;

import com.board.domain.image.PostImage;
import com.board.domain.post.Post;
import com.board.domain.category.Category;
import com.board.domain.member.Member;
import com.board.domain.member.Role;
import com.board.domain.post.PostLike;
import com.board.exception.auth.AuthInvalidMemberException;
import com.board.exception.member.MemberNotFoundException;
import com.board.exception.post.PostNotFoundException;
import com.board.exception.post.like.PostAlreadyLikesException;
import com.board.repository.category.CategoryRepository;
import com.board.repository.image.PostImageRepository;
import com.board.repository.member.MemberRepository;
import com.board.repository.post.PostRepository;
import com.board.repository.post.like.PostLikeRepository;
import com.board.requestDto.post.EditPostDto;
import com.board.requestServiceDto.Post.*;
import com.board.responseDto.Post.PostLikeResponseDto;
import com.board.responseDto.Post.PostResponseData;
import com.board.responseDto.Post.PostResponseDto;
import com.board.responseDto.Post.PostsResponseDto;
import com.board.responseDto.comment.CommentListResponseDto;
import com.board.responseDto.member.GetActivictyResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final CategoryService categoryService;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentService commentService;
    private final CategoryRepository categoryRepository;
    private final S3service s3service;

    //글 작성할때 캐시업데이트(키값으로 유저아이디)
    @Transactional
    @CachePut(value = "posts",key = "#newPostServiceDto.id")
    public GetActivictyResponseDto newPost(NewPostServiceDto newPostServiceDto, List<String> imgUrl){
        Member member=memberRepository.findById(newPostServiceDto.getId()).orElseThrow(MemberNotFoundException::new);
        //테스트DB 할때만 쓸꺼
        if(categoryRepository.count()==0){
            Category freeCategory=Category.builder()
                    .category("FREE")
                    .build();

            Category tradeCategory=Category.builder()
                    .category("TRADE")
                    .build();

            Category informationCategory=Category.builder()
                    .category("INFORMATION")
                    .build();

            categoryRepository.save(freeCategory);
            categoryRepository.save(tradeCategory);
            categoryRepository.save(informationCategory);
        }

        Category category=categoryService.getCategory(newPostServiceDto.getNewPostDto().getCategory());

        Post post=Post.builder()
                .member(member)
                .title(newPostServiceDto.getNewPostDto().getTitle())
                .content(newPostServiceDto.getNewPostDto().getContent())
                .category(category)
                .build();

        postRepository.save(post);

        //s3 이미지 파일 url post와 PostImage 테이블에 저장
        for(String url:imgUrl){
            PostImage postImage = PostImage.builder()
                    .post(post)
                    .imgFileName(url)
                    .build();
        }

        return memberRepository.getActivityPosts(newPostServiceDto.getId());
    }

    @Transactional
    public PostResponseDto getPost(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        int likesNum=postRepository.getLikesNum(post.getId());
        //조회수 +1
        post.plusView();

        //PostResponseData에 조회수 좋아요 인자 넣어서 반환
        PostResponseData postResponseData=post.toPostResponseData(likesNum);
        CommentListResponseDto commentListResponseDto=commentService.getCommentListRespone(postId);

        return PostResponseDto.builder()
                .postResponseData(postResponseData)
                .commentListResponseDto(commentListResponseDto)
                .build();
    }

    @Transactional
    public PostsResponseDto getList(GetPostsServiceDto getPostsServiceDto) {
        if(getPostsServiceDto.getCategory()==null){
            getPostsServiceDto.defaultCategory("FREE");
        }

        return postRepository.getPostsResponse(getPostsServiceDto);
    }


    //글 삭제할때도 캐시업데이트(키값으로 유저아이디)
    @Transactional
    @CacheEvict(value = "posts",key = "#deletePostServiceDto.id")
    public void deletePost(DeletePostServiceDto deletePostServiceDto) {
        Member member=memberRepository.findById(deletePostServiceDto.getId()).orElseThrow(MemberNotFoundException::new);
        Post post=postRepository.getPostById(deletePostServiceDto.getPostId());

        if((member==post.getMember())||(member.getRole()== Role.ADMIN)) {
            //s3에서 이미지 파일 삭제
            List<PostImage> imgUrls=post.getImgUrls();
            for(PostImage postImage:imgUrls){
                s3service.deleteFile(postImage.getImgFileName(),"raw");
            }

            postRepository.delete(post);
        }else{
            throw new AuthInvalidMemberException();
        }
    }

    //게시글이 수정되면 캐시도 업데이트!!
    @CachePut(value = "posts",key = "#editPostServiceDto.id")
    @Transactional
    public GetActivictyResponseDto editPost(EditPostServiceDto editPostServiceDto,List<String> upLoadListUrl) {
        Member member=memberRepository.findById(editPostServiceDto.getId()).orElseThrow(MemberNotFoundException::new);
        Post post=postRepository.getPostById(editPostServiceDto.getPostId());

        Category editCategory=categoryService.getCategory(editPostServiceDto.getEditPostDto().getCategory());

        EditPostDto editPostDto=editPostServiceDto.getEditPostDto();

        if((member==post.getMember())||(member.getRole()== Role.ADMIN)){
            post.edit(editPostDto.getTitle(),editPostDto.getContent(),editCategory);
            for(String url:upLoadListUrl){
                PostImage postImage = PostImage.builder()
                        .post(post)
                        .imgFileName(url)
                        .build();
            }
        }else{
            throw new AuthInvalidMemberException();
        }

        return memberRepository.getActivityPosts(editPostServiceDto.getId());
    }
    @Transactional
    public PostLikeResponseDto likesPost(LikesPostServiceDto likesPostServiceDto) {
        Post post=postRepository.findById(likesPostServiceDto.getPostId()).orElseThrow(PostNotFoundException::new);
        Member member=memberRepository.findById(likesPostServiceDto.getId()).orElseThrow(MemberNotFoundException::new);
        Optional<PostLike> checkPostLike=postLikeRepository.findByPostAndMember(post,member);

        if(checkPostLike.isPresent()){
            throw new PostAlreadyLikesException();
        }

        PostLike postLike=PostLike.builder()
                .islike(likesPostServiceDto.isLike())
                .post(post)
                .member(member)
                .build();

        postLikeRepository.save(postLike);
        int likeCnt=postRepository.getLikesNum(likesPostServiceDto.getPostId());

        return PostLikeResponseDto.builder()
                .likeCnt(likeCnt)
                .build();
    }




}
