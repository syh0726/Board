package com.example.board.domain.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {
    @Id
    @NotNull
    @Setter
    @Enumerated(EnumType.STRING)
    private CategoryName categoryName;

    @Builder
    public Category(String category)
    {
        this.categoryName = CategoryName.valueOf(category);
    }

    public void edit(String category) {
        this.categoryName=CategoryName.valueOf(category);
    }
}

