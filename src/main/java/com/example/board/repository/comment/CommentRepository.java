package com.example.board.repository.comment;

import com.example.board.domain.comment.Comment;
import com.example.board.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long>,CommentRepositoryCustom {
}
