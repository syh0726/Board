package com.example.board.repository.category;

import com.example.board.domain.category.Category;
import com.example.board.domain.category.CategoryName;

import java.util.Optional;

public interface CategoryRepositoryCustom {
    Category findByCategoryName(CategoryName categoryName);

}
