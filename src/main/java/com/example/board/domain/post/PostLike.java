package com.example.board.domain.post;

import com.example.board.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@IdClass(PostLikePk.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostLike {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    private boolean islike;
    @Builder
    public PostLike(Post post, Member member, boolean islike) {
        this.post = post;
        this.member = member;
        this.islike = islike;
    }
}
