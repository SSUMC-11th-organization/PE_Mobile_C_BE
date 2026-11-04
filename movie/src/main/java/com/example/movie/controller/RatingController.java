package com.example.movie.controller;

import com.example.movie.dto.RatingResponse;
import com.example.movie.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}