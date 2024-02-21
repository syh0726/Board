package com.board.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode{
    //@Valid 에러
    INVALID_INPUT_TYPE(400,"V001","입력값이 검증을 통과하지 못했습니다."),

    //권한관련 에러
    AUTH_INVALID_MEMBER(403,"A001","사용자가 일치 하지 않습니다. 권한이 없습니다."),
    SESSION_NOT_FOUND(404,"A002","세션값을 찾을 수 없습니다."),
    UN_AUTH(401,"A003","인증이 필요합니다"),

    //댓글 관련 에러
    COMMENT_NOT_FOUND(404,"CMT001","댓글이 존재하지 않습니다."),

    //유저 관련 에러
    ALREADY_EXIST_EMAIL(400,"M001","이미 존재하하는 이메일 입니다."),
    ALREADY_EXIST_NICKNAME(400,"M002","이미 존재하하는 닉네임 입니다."),
    ALREADY_EXIST_PHONENUMBER(400,"M003","이미 존재하하는 휴대폰 번호 입니다."),
    INVALID_SIGNIN_INFORMATION(400,"M004","잘못된 로그인 요청 입니다."),
    MEMBER_NOT_FOUND(404,"M005","존재하지 않는 계정입니다."),

    //게시물 관련 에러
    POST_NOT_FOUND(404,"P001","게시글이 존재하지 않습니다."),
    POST_ALREADY_LIKES(400,"P002","이미 추천또는 비추천한 게시글입니다."),
    CATEGORY_NOT_FOUND(404,"P003","존재하지 않는 카테고리 목록입니다.");

    private final int status;
    private final String code;
    private final String message;

}
