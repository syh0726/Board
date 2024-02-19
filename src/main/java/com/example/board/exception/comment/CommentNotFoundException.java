package com.example.board.exception.comment;

import com.example.board.exception.BoardException;

public class CommentNotFoundException extends BoardException {
    private final static String MESSAGE="댓글이 존재하지 않습니다.";
    public CommentNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
