package com.board.exception.image;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class ImageOverLength extends BoardException {
    public ImageOverLength() {
        super(ErrorCode.IMAGE_OVER_LENGTH);
    }
}
