package com.community.domain.board;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardSort {
    FREE("free", "자유"),
    FORUM("forum", "정보"),
    QNA("qna", "질문");

    private final String key;
    private final String value;

}
