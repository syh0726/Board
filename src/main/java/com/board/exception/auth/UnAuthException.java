package com.board.exception.auth;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class UnAuthException extends BoardException {
    public UnAuthException() {
        super(ErrorCode.UN_AUTH);
    }
}
