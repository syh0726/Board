package com.board.domain.post;

import com.board.domain.member.Member;
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
