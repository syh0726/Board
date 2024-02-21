package com.board.exception.comment;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class CommentNotFoundException extends BoardException {
    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }


}
