package com.example.movie.exception;

public class InvalidNicknameException extends RuntimeException {
    public InvalidNicknameException(String message) {
        super(message);
    }
}
