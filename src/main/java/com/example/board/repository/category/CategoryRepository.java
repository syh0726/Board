package com.example.board.repository.category;

import com.example.board.domain.category.Category;
import com.example.board.domain.category.CategoryName;
import com.example.board.domain.post.Post;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long>,CategoryRepositoryCustom {
}
