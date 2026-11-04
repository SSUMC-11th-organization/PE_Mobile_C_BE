package com.example.movie.dto;

import com.example.movie.entity.Rating;

import java.time.LocalDateTime;
import java.util.List;

public record RatingResponse(
        Long ratingId,
        Long movieId,
        Integer score,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static RatingResponse from(Rating rating) {
        return new RatingResponse(
                rating.getRatingId(),
                rating.getMovieId(),
                rating.getScore(),
                rating.getComment(),
                rating.getCreatedAt(),
                rating.getUpdatedAt()
        );
    }

    public record RatingPageResponse(
            List<RatingResponse> content,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean hasNext
    ) {}
}
