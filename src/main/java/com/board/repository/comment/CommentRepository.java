package com.board.repository.comment;

import com.board.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long>,CommentRepositoryCustom {
}
