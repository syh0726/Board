package com.example.board.exception.member;

import com.example.board.exception.BoardException;

public class AlreadyExistPhoneNumberException extends BoardException {

    private final static String MESSAGE="이미 존재하는 번호 입니다.";
    public AlreadyExistPhoneNumberException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
