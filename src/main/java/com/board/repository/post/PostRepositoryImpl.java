package com.board.repository.post;

import com.board.domain.image.QPostImage;
import com.board.domain.post.Post;
import com.board.responseDto.Post.PostsResponseDto;
import com.board.domain.category.CategoryName;
import com.board.domain.category.QCategory;
import com.board.domain.member.QMember;
import com.board.domain.post.QPost;
import com.board.domain.post.QPostLike;
import com.board.repository.comment.CommentRepository;
import com.board.requestServiceDto.Post.GetPostsServiceDto;
import com.board.responseDto.Post.PostListItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    private final CommentRepository commentRepository;
    @Override
    public PostsResponseDto getPostsResponse(GetPostsServiceDto getPostsServiceDto) {
        List<PostListItem> postListItems= new ArrayList<>();

        //카테고리에 있는 글들
        JPAQuery<Post> query=jpaQueryFactory.selectFrom(QPost.post)
                .where(QPost.post.category.categoryName.eq(CategoryName.valueOf(getPostsServiceDto.getCategory())));

        if(getPostsServiceDto.getGetPostsDto().getCondition()!=null) {
            if(getPostsServiceDto.getGetPostsDto().getCondition().equals("TITLE")){
                query.where(QPost.post.title.contains(getPostsServiceDto.getGetPostsDto().getKeyword()));
            }else if(getPostsServiceDto.getGetPostsDto().getCondition().equals("CONTENT")){
                query.where(QPost.post.content.contains(getPostsServiceDto.getGetPostsDto().getKeyword()));
            }else if(getPostsServiceDto.getGetPostsDto().getCondition().equals("NICKNAME")) {
                query.where(QPost.post.member.nickName.contains(getPostsServiceDto.getGetPostsDto().getKeyword()));
            } else{
                query.where(QPost.post.title.contains(getPostsServiceDto.getGetPostsDto().getKeyword())
                        .or(QPost.post.content.contains(getPostsServiceDto.getGetPostsDto().getKeyword())));
            }

        }

        int totalPost=query.fetch().size();
        int maxPage=(int) Math.floorDiv(totalPost, getPostsServiceDto.getSize());
        if(totalPost%getPostsServiceDto.getSize()!=0) {
            maxPage+= 1;
        }

        //카테고리에 있는 글들 페이징
        List<Post> posts=query
                .offset(getPostsServiceDto.getOffset())
                .limit(getPostsServiceDto.getSize())
                .orderBy(QPost.post.id.desc())
                .fetch();

        for(Post post:posts){
            postListItems.add(PostListItem.builder()
                            .post(post)
                            .commentNum(commentRepository.getCommentsNum(post.getId()))
                            .likeCnt(getLikesNum(post.getId()))
                            .build());
        }

        return PostsResponseDto.builder()
                .postListItems(postListItems)
                .maxPage(maxPage)
                .build();
    }

    @Override
    public int getLikesNum(Long postId) {
        int likes=jpaQueryFactory.selectFrom(QPostLike.postLike)
                .innerJoin(QPostLike.postLike.post,QPost.post)
                .where(QPostLike.postLike.islike.eq(true),
                        QPostLike.postLike.post.id.eq(postId))
                .fetch().size();

        int disLikes=jpaQueryFactory.selectFrom(QPostLike.postLike)
                .innerJoin(QPostLike.postLike.post,QPost.post)
                .where(QPostLike.postLike.islike.eq(false),
                        QPostLike.postLike.post.id.eq(postId))
                .fetch().size();

        return likes-disLikes;
    }

    @Override
    public Post getPostById(Long postId) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .leftJoin(QPost.post.member, QMember.member)
                .fetchJoin()
                .leftJoin(QPost.post.category, QCategory.category)
                .fetchJoin()
                .leftJoin(QPost.post.imgUrls, QPostImage.postImage)
                .fetchJoin()
                .where(QPost.post.id.eq(postId))
                .distinct()
                .fetchOne();
    }



}
