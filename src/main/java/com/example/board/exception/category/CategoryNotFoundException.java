package com.example.board.exception.category;

import com.example.board.exception.BoardException;

public class CategoryNotFoundException extends BoardException {
    private static final String MESSAGE="존재하지 않는 카테고리 목록입니다.";

    public CategoryNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
