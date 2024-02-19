package com.example.board.exception.member;

import com.example.board.exception.BoardException;

public class MemberNotFoundException extends BoardException {
    private static final String MESSAGE="계정이 존재하지 않습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }
}
