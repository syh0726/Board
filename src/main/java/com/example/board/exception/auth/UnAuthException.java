package com.example.board.exception.auth;

import com.example.board.exception.BoardException;

public class UnAuthException extends BoardException {
    private final static String MESSAGE="인증이 필요합니다.";
    public UnAuthException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 401;
    }

}
