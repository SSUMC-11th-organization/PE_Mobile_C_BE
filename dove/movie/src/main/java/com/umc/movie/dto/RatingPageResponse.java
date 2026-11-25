package com.umc.movie.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record RatingPageResponse(
        int page,
        int size,
        Long totalElements,
        int totalPages,
        List<RatingResponse> ratings
) {
    public static RatingPageResponse from(Page<RatingResponse> ratingPage) {
        return new RatingPageResponse(
                ratingPage.getNumber(),
                ratingPage.getSize(),
                ratingPage.getTotalElements(),
                ratingPage.getTotalPages(),
                ratingPage.getContent()
        );
    }
}