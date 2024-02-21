package com.board.exception;

import lombok.Getter;

@Getter
public abstract class BoardException extends RuntimeException {

    private final ErrorCode errorCode;
    public BoardException(ErrorCode errorCode) {
        this.errorCode=errorCode;
    }
}
