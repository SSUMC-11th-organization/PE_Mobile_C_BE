package com.example.movie.exception;

import com.example.movie.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import com.example.movie.exception.ApiException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(ApiResponse.failure("COMMON4001", message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException exception) {
        String cause = exception.getMostSpecificCause().getMessage();

        if (cause != null && cause.contains("for key 'member.nickname'")) {
            return ResponseEntity.status(409).body(ApiResponse.failure("COMMON4093", "이미 사용 중인 닉네임입니다."));
        }
        if (cause != null && cause.contains("for key 'member.email'")) {
            return ResponseEntity.status(409).body(ApiResponse.failure("COMMON4094", "이미 사용 중인 이메일입니다."));
        }
        return ResponseEntity.status(409).body(ApiResponse.failure("COMMON4091", "이미 존재하는 데이터입니다."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.status(409).body(ApiResponse.failure("COMMON4092", exception.getMessage()));
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateNickname(DuplicateNicknameException exception) {
        return ResponseEntity.status(409).body(ApiResponse.failure("COMMON4093", exception.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public  ResponseEntity<ApiResponse<Void>> handleDuplicateEmail(DuplicateEmailException exception) {
        return  ResponseEntity.status(409).body(ApiResponse.failure("COMMON4094", exception.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleResponseStatus(ResponseStatusException exception) {
        int status = exception.getStatusCode().value();
        return ResponseEntity.status(status)
                .body(ApiResponse.failure("COMMON" + status, exception.getReason()));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(ApiResponse.failure(exception.getCode(), exception.getMessage()));
    }
}