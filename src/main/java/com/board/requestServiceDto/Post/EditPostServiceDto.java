package com.board.requestServiceDto.Post;

import com.board.requestDto.post.EditPostDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class EditPostServiceDto {
    private final long postId;

    private final long id;

    private final EditPostDto editPostDto;

    @Builder
    public EditPostServiceDto(long postId, long id, EditPostDto editPostDto) {
        this.postId = postId;
        this.id = id;
        this.editPostDto = editPostDto;
    }
}
