package com.board.repository.category;

import com.board.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long>,CategoryRepositoryCustom {
}
