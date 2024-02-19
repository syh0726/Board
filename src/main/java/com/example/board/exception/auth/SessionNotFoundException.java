package com.example.board.exception.auth;

import com.example.board.exception.BoardException;

public class SessionNotFoundException extends BoardException {

    private final static String MESSAGE="세션을 DB에서 찾을 수 없습니다.";
    public SessionNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
