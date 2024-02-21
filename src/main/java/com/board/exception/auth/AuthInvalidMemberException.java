package com.board.exception.auth;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class AuthInvalidMemberException extends BoardException {
    public AuthInvalidMemberException() {
        super(ErrorCode.AUTH_INVALID_MEMBER);
    }
}
