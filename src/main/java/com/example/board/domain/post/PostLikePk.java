package com.example.board.domain.post;

import com.example.board.domain.member.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class PostLikePk implements Serializable {
    private Post post;
    private Member member;

    @Builder
    public PostLikePk(Post post, Member member) {
        this.post = post;
        this.member = member;
    }
}
