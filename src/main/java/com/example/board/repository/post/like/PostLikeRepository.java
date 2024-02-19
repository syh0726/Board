package com.example.board.repository.post.like;

import com.example.board.domain.member.Member;
import com.example.board.domain.post.Post;
import com.example.board.domain.post.PostLike;
import com.example.board.domain.post.PostLikePk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikePk> {
    Optional<PostLike> findByPostAndMember(Post post, Member member);
}
