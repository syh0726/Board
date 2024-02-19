package com.example.board.exception.member;

import com.example.board.exception.BoardException;

public class AlreadyExistEmailException extends BoardException {
    private final static String MESSAGE="이미 존재하는 이메일 입니다.";
    public AlreadyExistEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
