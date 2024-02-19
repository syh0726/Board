package com.example.board.exception.member;

import com.example.board.exception.BoardException;

public class InvalidSigninInformationException extends BoardException {

    private static final String MESSAGE="잘못된 로그인 요청입니다.";

    public InvalidSigninInformationException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
