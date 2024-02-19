package com.example.board.exception.post;

import com.example.board.exception.BoardException;

public class PostNotFoundException extends BoardException {

    private final static String MESSAGE="게시글이 존재하지 않습니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }
}
