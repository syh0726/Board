package com.example.board.repository.post;

import com.example.board.domain.category.Category;
import com.example.board.domain.post.Post;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface PostRepository extends JpaRepository<Post,Long>,PostRepositoryCustom {

}
