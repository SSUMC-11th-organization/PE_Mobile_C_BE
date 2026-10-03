package com.umc.study.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    //400 BAD_REQUEST: 잘못된 요청
    INVALID_CATEGORY_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다."),

    //409 CONFLICT: 데이터 중복
    DUPLICATE_TITLE(HttpStatus.CONFLICT, "이미 존재하는 책입니다.");

    private final HttpStatus status;
    private final String message;
}
