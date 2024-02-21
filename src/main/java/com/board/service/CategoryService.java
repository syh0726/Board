package com.board.service;

import com.board.repository.category.CategoryRepository;
import com.board.domain.category.Category;
import com.board.domain.category.CategoryName;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category getCategory(String categoryNameValue) {
        CategoryName categoryName=CategoryName.valueOf(categoryNameValue);
        return categoryRepository.findByCategoryName(categoryName);
    }
}
