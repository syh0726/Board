package com.board.exception.member;

import com.board.exception.BoardException;
import com.board.exception.ErrorCode;

public class MemberNotFoundException extends BoardException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
