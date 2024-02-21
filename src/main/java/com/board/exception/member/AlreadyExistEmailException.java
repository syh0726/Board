package com.board.exception.member;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class AlreadyExistEmailException extends BoardException {
    public AlreadyExistEmailException() {
        super(ErrorCode.ALREADY_EXIST_EMAIL);
    }
}
