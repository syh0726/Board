package com.example.board.domain.post;

import com.example.board.domain.category.Category;
import com.example.board.domain.member.Member;
import com.example.board.responseDto.Post.PostResponseData;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
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
        return PostResponseData.builder()
                .createdAt(this.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .nickName(this.member.getNickName())
                .viewCnt(this.viewCnt)
                .category(this.getCategory().getCategoryName().getCategory())
                .content(this.content)
                .id(this.id)
                .title(this.title)
                .authorId(this.member.getId())
                .likeCnt(likeCnt)
                .build();
    }

    public void plusView() {
        this.viewCnt+=1;
    }
}
