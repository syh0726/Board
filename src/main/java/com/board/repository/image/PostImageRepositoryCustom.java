package com.board.repository.image;

import com.board.domain.image.PostImage;

public interface PostImageRepositoryCustom {

    PostImage findByName(String name);
}
