package com.board.repository.image;

import com.board.domain.image.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage,Long>,PostImageRepositoryCustom {
}
