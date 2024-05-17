package com.board.exception.image;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class InvalidUploadImage extends BoardException {
    public InvalidUploadImage() {
        super(ErrorCode.INVALID_UPLOAD_IMAGE);
    }
}
