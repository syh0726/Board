package com.board.repository.category;

import com.board.domain.category.Category;
import com.board.domain.category.CategoryName;

public interface CategoryRepositoryCustom {
    Category findByCategoryName(CategoryName categoryName);

}
