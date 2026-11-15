package com.umc.movie.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // MEMBER
    // CONFLICT
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "MEMBER409_1", "이미 사용 중인 이메일입니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "MEMBER409_2", "이미 사용 중인 닉네임입니다."),

    // NOT_FOUND
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_1", "존재하지 않는 유저입니다."),

    // FORBIDDEN


    // RATING
    // CONFLICT
    RATING_DUPLICATED(HttpStatus.CONFLICT, "RATING409_1", "이미 이 영화에 평점을 남기셨습니다."),

    // NOT_FOUND
    RATING_NOT_FOUND(HttpStatus.NOT_FOUND, "RATING404_1", "존재하지 않는 평점입니다."),

    // FORBIDDEN
    RATING_FORBIDDEN(HttpStatus.FORBIDDEN, "RATING403_1", "본인이 작성한 평점만 수정하거나 삭제할 수 있습니다."),

    // AUTH
    // UNAUTHORIZED
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH401_1", "이메일 또는 비밀번호가 올바르지 않습니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "AUTH401_2", "로그인이 필요한 요청입니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH401_3", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH401_4", "토큰이 만료되었습니다. 다시 로그인해주세요.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
