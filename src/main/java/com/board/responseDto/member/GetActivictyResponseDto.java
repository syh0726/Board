package com.board.responseDto.member;

import com.board.responseDto.Post.ActivityPostList;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class GetActivictyResponseDto {
    private final String nickName;
    private final List<ActivityPostList> postList;

    @Builder
    public GetActivictyResponseDto(String nickName, List<ActivityPostList> postList) {
        this.nickName = nickName;
        this.postList = postList;
    }
}
