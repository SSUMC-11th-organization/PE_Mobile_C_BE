package com.example.movie.service;

import com.example.movie.dto.RatingResponse;
import com.example.movie.entity.Rating;
import com.example.movie.repository.RatingRepository;
import com.example.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MemberRepository memberRepository;

//    @Transactional(readOnly = true)
//    public List<RatingResponse> getRatingsByMember(Long memberId) {
//        if (!memberRepository.existsById(memberId)) {
//            // @RestControllerAdvice로 예외를 한 곳에서 다루는 방법은 6주차에 배웁니다.
//            // 지금은 Spring이 기본 제공하는 ResponseStatusException으로 404를 바로 응답합니다.
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.");
//        }
//
//        return ratingRepository.findByMember_MemberIdOrderByRatingIdDesc(memberId).stream()
//                .map(RatingResponse::from)
//                .toList();
//    }

    @Transactional(readOnly = true)
    public RatingResponse.RatingPageResponse getRatingsByMember(Long memberId, Pageable pageable) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.");
        }

        Page<Rating> page = ratingRepository.findByMember_MemberIdOrderByRatingIdDesc(memberId, pageable);

        return new RatingResponse.RatingPageResponse(
                page.getContent().stream().map(RatingResponse::from).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }
}