package com.board.exception.image;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class ImageOverSize extends BoardException {

    public ImageOverSize() {
        super(ErrorCode.IMAGE_OVER_SIZE);
    }
}
