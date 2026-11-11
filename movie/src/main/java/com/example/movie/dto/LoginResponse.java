package com.example.movie.dto;

public record LoginResponse(
        String nickname,
        String accessToken
) {
}