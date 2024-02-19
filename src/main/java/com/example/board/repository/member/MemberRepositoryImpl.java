package com.example.board.repository.member;

import com.example.board.domain.auth.QSession;
import com.example.board.domain.member.Member;
import com.example.board.domain.member.QMember;
import com.example.board.domain.post.QPost;
import com.example.board.requestDto.member.FindPasswordDto;
import com.example.board.responseDto.Post.ActivityPostList;
import com.example.board.responseDto.member.GetActivictyResponseDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Member findByEmail(String email) {
        return jpaQueryFactory.selectFrom(QMember.member)
                .where(QMember.member.email.eq(email))
                .fetchOne();
    }

    @Override
    public Member findByPhoneNumber(String phoneNumber) {
        return jpaQueryFactory.selectFrom(QMember.member)
                .where(QMember.member.phoneNumber.eq(phoneNumber))
                .fetchOne();
    }

    @Override
    public Member findByNickname(String nickName) {
        return jpaQueryFactory.selectFrom(QMember.member)
                .where(QMember.member.nickName.eq(nickName))
                .fetchOne();
    }

    @Override
    public Member getMemberbyId(long id) {
        return jpaQueryFactory.selectFrom(QMember.member)
                .leftJoin(QMember.member.sessions, QSession.session)
                .fetchJoin()
                .where(QMember.member.id.eq(id))
                .distinct()
                .fetchOne();
    }

    @Override
    public Member findBAccessToken(String accessToken) {
        Member member=jpaQueryFactory.selectFrom(QMember.member)
                .leftJoin(QMember.member.sessions,QSession.session)
                .fetchJoin()
                .where(QMember.member.sessions.any().accessToken.eq(accessToken))
                .distinct()
                .fetchOne();

        return member;
    }

    @Override
    public GetActivictyResponseDto getActivityPosts(long id) {
       List<ActivityPostList> activityPostLists=new ArrayList<>();

        List<Tuple> posts=jpaQueryFactory.select(
                QPost.post.title
                ,QPost.post.id
                ,QPost.post.createdAt)
                .from(QPost.post)
                .where(QPost.post.member.id.eq(id))
                .fetch();

        String nickName=jpaQueryFactory.select(QMember.member.nickName)
                        .from(QMember.member)
                        .where(QMember.member.id.eq(id))
                        .fetchOne();


        for(Tuple tuple:posts){
            Long postId=tuple.get(QPost.post.id);
            String title=tuple.get(QPost.post.title);
            String createdAt= Objects.requireNonNull(tuple.get(QPost.post.createdAt)).format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss"));

            activityPostLists.add(ActivityPostList.builder()
                            .postId(postId)
                            .createdAt(createdAt)
                            .title(title)
                            .build());
        }

        return GetActivictyResponseDto.builder()
                .nickName(nickName)
                .postList(activityPostLists)
                .build();
    }

    @Override
    public boolean findByEmailAndPhoneNumber(FindPasswordDto findPasswordDto) {
        Member member=jpaQueryFactory.selectFrom(QMember.member)
                .where(QMember.member.email.eq(findPasswordDto.getEmail()),
                        QMember.member.phoneNumber.eq(findPasswordDto.getPhoneNumber()))
                .fetchOne();

        if(member!=null){
            return true;
        }else{
            return false;
        }
    }


}
