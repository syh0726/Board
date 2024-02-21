package com.board.exception.member;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class AlreadyExistPhoneNumberException extends BoardException {

    public AlreadyExistPhoneNumberException() {
        super(ErrorCode.ALREADY_EXIST_PHONENUMBER);
    }
}
