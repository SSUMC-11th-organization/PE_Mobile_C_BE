package com.umc.study.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 입력입니다."),

    // Category
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY404", "존재하지 않는 카테고리입니다."),

    // Book
    DUPLICATE_BOOK_TITLE(HttpStatus.CONFLICT, "BOOK409", "이미 존재하는 도서 제목입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
