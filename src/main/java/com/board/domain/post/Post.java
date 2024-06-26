package com.board.domain.post;

import com.board.domain.category.Category;
import com.board.domain.image.PostImage;
import com.board.domain.member.Member;
import com.board.responseDto.Post.PostResponseData;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @NotNull
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private int viewCnt;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "post",fetch = FetchType.LAZY)
    List<PostImage> imgUrls=new ArrayList<>();
    @Builder
    public Post(String title, String content, Member member,Category category) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.viewCnt = 0;
    }

    public void edit(String title,String content,Category category){
        this.title=title;
        this.content=content;
        this.category=category;
    }

    public PostResponseData toPostResponseData(int likeCnt){
        List<String> list=new ArrayList<>();
        for(PostImage postImage : this.imgUrls){
            list.add(postImage.getImgFileName());
        }

        return PostResponseData.builder()
                .createdAt(this.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .nickName(this.member.getNickName())
                .viewCnt(this.viewCnt)
                .category(this.getCategory().getCategoryName().getCategory())
                .content(this.content)
                .id(this.id)
                .title(this.title)
                .authorId(this.member.getId())
                .imgUrls(list)
                .likeCnt(likeCnt)
                .build();
    }

    public void plusView() {
        this.viewCnt+=1;
    }
}
