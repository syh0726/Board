package com.board.service;

import com.board.domain.member.Role;
import com.board.domain.post.Post;
import com.board.domain.comment.Comment;
import com.board.domain.comment.CommentEditor;
import com.board.domain.member.Member;
import com.board.exception.auth.AuthInvalidMemberException;
import com.board.exception.member.MemberNotFoundException;
import com.board.exception.post.PostNotFoundException;
import com.board.repository.comment.CommentRepository;
import com.board.repository.member.MemberRepository;
import com.board.repository.post.PostRepository;
import com.board.requestServiceDto.comment.DeleteCommentServiceDto;
import com.board.requestServiceDto.comment.EditCommentServiceDto;
import com.board.requestServiceDto.comment.NewCommentServiceDto;
import com.board.responseDto.comment.CommentListItem;
import com.board.responseDto.comment.CommentListResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Transactional
    public Comment newComment(NewCommentServiceDto newCommentServiceDto){
        Member member=memberRepository.findById(newCommentServiceDto.getId()).orElseThrow(MemberNotFoundException::new);
        Post post=postRepository.findById(newCommentServiceDto.getPostId()).orElseThrow(PostNotFoundException::new);

        Comment comment=Comment.builder()
                .post(post)
                .member(member)
                .content(newCommentServiceDto.getNewCommentDto().getContent())
                .build();

        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public Comment editComment(EditCommentServiceDto editCommentServiceDto) {
        Member member=memberRepository.findById(editCommentServiceDto.getId()).orElseThrow(MemberNotFoundException::new);
        Comment comment=commentRepository.getCommentById(editCommentServiceDto.getCommentId());

        CommentEditor commentEditor= CommentEditor.builder()
                .content(editCommentServiceDto.getEditCommentDto().getContent())
                .build();

        if(member==comment.getMember()||(member.getRole()== Role.ADMIN)){
            comment.edit(commentEditor);
        }else{
            throw new AuthInvalidMemberException();
        }

        return comment;
    }
    @Transactional
    public void deleteComment(DeleteCommentServiceDto deleteCommentServiceDto) {
        Member member=memberRepository.findById(deleteCommentServiceDto.getId()).orElseThrow(MemberNotFoundException::new);
        Comment comment=commentRepository.getCommentById(deleteCommentServiceDto.getCommentId());

        if(member==comment.getMember()||(member.getRole()== Role.ADMIN)) {
            commentRepository.deleteById(deleteCommentServiceDto.getCommentId());
        }else{
            throw new AuthInvalidMemberException();
        }
    }

    @Transactional
    public CommentListResponseDto getCommentListRespone(Long postId){
        Post post=postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<Comment> comments=commentRepository.findByPost(post);
        List<CommentListItem> commentListItems=new ArrayList<>();
        int commentsNum=commentRepository.getCommentsNum(post.getId());

        for(Comment comment : comments){
            commentListItems.add(CommentListItem.builder()
                            .comment(comment)
                            .build());
        }

        return CommentListResponseDto.builder()
                .commentsNum(commentsNum)
                .commentListItems(commentListItems)
                .build();

    }

}
