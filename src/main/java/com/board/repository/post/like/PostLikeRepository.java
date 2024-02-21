package com.board.repository.post.like;

import com.board.domain.member.Member;
import com.board.domain.post.Post;
import com.board.domain.post.PostLike;
import com.board.domain.post.PostLikePk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikePk> {
    Optional<PostLike> findByPostAndMember(Post post, Member member);
}
