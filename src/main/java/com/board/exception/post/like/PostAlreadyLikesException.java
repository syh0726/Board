package com.board.exception.post.like;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class PostAlreadyLikesException extends BoardException {

    public PostAlreadyLikesException() {
        super(ErrorCode.POST_ALREADY_LIKES);
    }


}
