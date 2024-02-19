package com.example.board.domain.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentEditor {
    private final String content;

    @Builder
    public CommentEditor(String content) {
        this.content = content;
    }
}
