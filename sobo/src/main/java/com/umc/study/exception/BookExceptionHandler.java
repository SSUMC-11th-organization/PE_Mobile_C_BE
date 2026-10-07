package com.umc.study.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class BookExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(
            ConstraintViolationException exception
    ) {
        String name = exception.getConstraintName();

        if (name != null) {
            name = name.replace("`", "").replace("'", "").replace("\"", "");

            if (name.equals("uk_book_title")
                    || name.endsWith(".uk_book_title")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of(
                                "message", "이미 등록된 도서 제목입니다."
                        ));
            }
        }

        throw exception;
    }
}