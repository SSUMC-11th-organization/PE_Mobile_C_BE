package com.umc.movie.controller;

import com.umc.movie.dto.*;
import com.umc.movie.interceptor.AuthInterceptor;
import com.umc.movie.service.RatingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/members/{memberId}/ratings")
    public RatingPageResponse getRatingsByMember(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return ratingService.getRatingsByMember(memberId, page, size);
    }

    @PostMapping("/ratings")
    public ResponseEntity<ApiResponse<RatingResponse>> create(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @Valid @RequestBody CreateRatingRequest request) {
        RatingResponse response = ratingService.create(memberId, request);
        return ResponseEntity.status(201).body(ApiResponse.created("평점 등록에 성공했습니다.", response));
    }

    @PatchMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<RatingResponse>> update(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request) {
        RatingResponse response = ratingService.update(ratingId, memberId, request);
        return ResponseEntity.ok(ApiResponse.ok("평점 수정에 성공했습니다.", response));
    }

    @DeleteMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long ratingId) {
        ratingService.delete(ratingId, memberId);
        return ResponseEntity.ok(ApiResponse.ok("평점을 삭제했습니다.", null)); // 요청 성공, 돌려줄 내용은 없음
    }
}
