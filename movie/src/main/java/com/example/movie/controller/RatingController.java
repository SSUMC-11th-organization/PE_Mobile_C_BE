package com.example.movie.controller;

import com.example.movie.dto.ApiResponse;
import com.example.movie.dto.CreateRatingRequest;
import com.example.movie.dto.RatingResponse;
import com.example.movie.dto.UpdateRatingRequest;
import com.example.movie.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class RatingController {

    private final RatingService ratingService;

//    @GetMapping("/{memberId}/ratings")
//    public List<RatingResponse> getMemberRatings(@PathVariable Long memberId) {
//        return ratingService.getRatingsByMember(memberId);
//    }

    @GetMapping("/{memberId}/ratings")
    public RatingResponse.RatingPageResponse getMemberRatings(
            @PathVariable Long memberId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ratingService.getRatingsByMember(memberId, pageable);
    }

    @PostMapping("/{memberId}/ratings")
    public ResponseEntity<ApiResponse<Long>> create(
            @PathVariable Long memberId,
            @Valid @RequestBody CreateRatingRequest request
    ) {
        Long ratingId = ratingService.create(memberId, request);
        return ResponseEntity.status(201).body(ApiResponse.created("평점 등록에 성공했습니다.", ratingId));
    }

    @PatchMapping("/{memberId}/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable Long memberId,
            @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request
    ) {
        ratingService.update(ratingId, memberId, request);
        return ResponseEntity.ok(ApiResponse.ok("평점 수정에 성공했습니다.", null));
    }

    @DeleteMapping("/{memberId}/ratings/{ratingId}")
    public ResponseEntity<Void> delete(@PathVariable Long memberId, @PathVariable Long ratingId) {
        ratingService.delete(ratingId, memberId);
        return ResponseEntity.noContent().build();
    }
}