package com.board.exception.member;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class AlreadyExistNicknameException extends BoardException {
    public AlreadyExistNicknameException() {
        super(ErrorCode.ALREADY_EXIST_NICKNAME);
    }
}
