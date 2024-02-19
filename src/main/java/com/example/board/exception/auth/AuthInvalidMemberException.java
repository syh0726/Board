package com.example.board.exception.auth;

import com.example.board.exception.BoardException;

public class AuthInvalidMemberException extends BoardException {

    final static String MESSAGE="사용자가 일치 하지 않습니다. 권한이 없습니다.";

    public AuthInvalidMemberException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
