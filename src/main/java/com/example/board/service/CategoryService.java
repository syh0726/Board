package com.example.board.service;

import com.example.board.domain.category.Category;
import com.example.board.domain.category.CategoryName;
import com.example.board.domain.post.Post;
import com.example.board.exception.category.CategoryNotFoundException;
import com.example.board.repository.category.CategoryRepository;
import com.example.board.requestDto.post.NewPostDto;
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
