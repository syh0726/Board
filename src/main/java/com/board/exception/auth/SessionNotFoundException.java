package com.board.exception.auth;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class SessionNotFoundException extends BoardException {
    public SessionNotFoundException() {
        super(ErrorCode.SESSION_NOT_FOUND);
    }
}
