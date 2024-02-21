package com.board.exception.member;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class InvalidSigninInformationException extends BoardException {
    public InvalidSigninInformationException() {
        super(ErrorCode.INVALID_SIGNIN_INFORMATION);
    }

}
