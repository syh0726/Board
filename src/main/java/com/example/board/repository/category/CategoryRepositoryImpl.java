package com.example.board.repository.category;

import com.example.board.domain.category.Category;
import com.example.board.domain.category.CategoryName;
import com.example.board.domain.category.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Category findByCategoryName(CategoryName categoryName) {
        return jpaQueryFactory.selectFrom(QCategory.category)
                .where(QCategory.category.categoryName.eq(categoryName))
                .fetchOne();
    }
}
