package com.board.exception.post;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class PostNotFoundException extends BoardException {

    public PostNotFoundException() {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
