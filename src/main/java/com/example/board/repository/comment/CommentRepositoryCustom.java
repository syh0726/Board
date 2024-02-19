package com.example.board.repository.comment;

import com.example.board.domain.comment.Comment;
import com.example.board.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {
    List<Comment> findByPost(Post post);

    int getCommentsNum (Long postId);

    Comment getCommentById(Long commentId);
}
