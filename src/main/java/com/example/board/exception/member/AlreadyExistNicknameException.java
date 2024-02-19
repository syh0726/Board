package com.example.board.exception.member;

import com.example.board.exception.BoardException;

public class AlreadyExistNicknameException extends BoardException {
    private final static String MESSAGE="이미 존재하는 닉네임 입니다.";
    public AlreadyExistNicknameException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
