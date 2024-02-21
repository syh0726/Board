package com.board.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum CategoryName {
    FREE("자유"),
    TRADE("거래"),
    INFORMATION("정보");

    private final String category;
}
