package com.example.board.repository.comment;

import com.example.board.domain.comment.Comment;
import com.example.board.domain.comment.QComment;
import com.example.board.domain.member.QMember;
import com.example.board.domain.post.Post;
import com.example.board.domain.post.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;


    //fetchjoin으로 N+1문제 해결
    @Override
    public List<Comment> findByPost(Post post) {
        return  jpaQueryFactory.selectFrom(QComment.comment)
                    .leftJoin(QComment.comment.member, QMember.member)
                    .where(QPost.post.eq(post))
                    .fetchJoin()
                    .distinct()
                    .fetch();
    }
    @Override
    public int getCommentsNum(Long postId) {
        return jpaQueryFactory.selectFrom(QComment.comment)
                .innerJoin(QComment.comment.post,QPost.post)
                .where(QPost.post.id.eq(postId))
                .fetch().size();
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return jpaQueryFactory.selectFrom(QComment.comment)
                .leftJoin(QComment.comment.member,QMember.member)
                .where(QComment.comment.id.eq(commentId))
                .fetchJoin()
                .distinct()
                .fetchOne();
    }

}
