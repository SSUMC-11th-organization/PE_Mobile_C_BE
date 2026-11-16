package com.example.movie.dto;

public record MovieRecordResponse(
        RatingResponse rating,
        FavoriteResponse favorite
) {
}
