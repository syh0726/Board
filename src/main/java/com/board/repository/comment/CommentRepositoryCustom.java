package com.board.repository.comment;

import com.board.domain.post.Post;
import com.board.domain.comment.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findByPost(Post post);

    int getCommentsNum (Long postId);

    Comment getCommentById(Long commentId);
}
