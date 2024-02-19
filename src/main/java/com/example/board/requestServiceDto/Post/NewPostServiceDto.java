package com.example.board.requestServiceDto.Post;

import com.example.board.config.data.MemberSession;
import com.example.board.requestDto.post.NewPostDto;
import lombok.Builder;
import lombok.Getter;


@Getter
public class NewPostServiceDto {
    private final NewPostDto newPostDto;
    private final Long id;
    @Builder
    public NewPostServiceDto(NewPostDto newPostDto, Long id) {
        this.newPostDto = newPostDto;
        this.id = id;
    }
}
