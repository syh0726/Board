package com.example.board.exception.post.like;

import com.example.board.exception.BoardException;

public class PostAlreadyLikesException extends BoardException {

    private final static String MESSAGE="추천또는 비추천은 게시글에 한번만 가능합니다. ";
    public PostAlreadyLikesException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
