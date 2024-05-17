package com.board.domain.image;

import com.board.domain.post.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    //포스트에 이미지 추가
    @Builder
    public PostImage(Post post,String imgFileName){
        this.post=post;
        this.imgFileName=imgFileName;
        post.getImgUrls().add(this);
    }

}
