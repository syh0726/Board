package com.board.controller;

import com.board.config.data.MemberSession;
import com.board.responseDto.comment.CommentListResponseDto;
import com.board.service.CommentService;
import com.board.requestDto.comment.EditCommentDto;
import com.board.requestDto.comment.NewCommentDto;
import com.board.requestServiceDto.comment.DeleteCommentServiceDto;
import com.board.requestServiceDto.comment.EditCommentServiceDto;
import com.board.requestServiceDto.comment.NewCommentServiceDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public CommentListResponseDto newComment(@PathVariable(name = "postId") Long postId,
                                             @RequestBody @Valid NewCommentDto newCommentDto
                           , MemberSession memberSession){

        NewCommentServiceDto newCommentServiceDto=NewCommentServiceDto.builder()
                .newCommentDto(newCommentDto)
                .postId(postId)
                .id(memberSession.id)
                .build();

        commentService.newComment(newCommentServiceDto);

        return commentService.getCommentListRespone(postId);
    }

    @PatchMapping("/posts/{postId}/comments/{commentId}")
    public CommentListResponseDto editComment(@RequestBody @Valid EditCommentDto editCommentDto,
                                              @PathVariable(name = "postId") Long postId,
                                              @PathVariable(name = "commentId") Long commentId,
                                              MemberSession memberSession){

        EditCommentServiceDto editCommentServiceDto=EditCommentServiceDto.builder()
                .editCommentDto(editCommentDto)
                .id(memberSession.id)
                .commentId(commentId)
                .build();

        commentService.editComment(editCommentServiceDto);

        return commentService.getCommentListRespone(postId);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public CommentListResponseDto deleteComment(MemberSession memberSession,
                                                @PathVariable(name="postId") Long postId,
                                                @PathVariable(name = "commentId") Long commentId){

        DeleteCommentServiceDto deleteCommentServiceDto= DeleteCommentServiceDto.builder()
                .commentId(commentId)
                .id(memberSession.id)
                .build();

        commentService.deleteComment(deleteCommentServiceDto);

        return commentService.getCommentListRespone(postId);
    }
}
