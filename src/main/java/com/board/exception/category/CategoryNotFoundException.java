package com.board.exception.category;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class CategoryNotFoundException extends BoardException {

    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND);
    }
}
