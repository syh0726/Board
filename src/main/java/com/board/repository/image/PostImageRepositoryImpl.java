package com.board.repository.image;

import com.board.domain.image.PostImage;
import com.board.domain.image.QPostImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostImageRepositoryImpl implements PostImageRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PostImage findByName(String name) {
        return jpaQueryFactory.selectFrom(QPostImage.postImage)
                .where(QPostImage.postImage.imgFileName.eq(name))
                .fetchOne();
    }
}
