// src/main/java/com/umc/movie/repository/RatingRepository.java
package com.umc.movie.repository;

import com.umc.movie.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMember_MemberIdOrderByRatingIdDesc(Long memberId);

    // 언더스코어(_)는 rating.member.memberId 처럼 연관된 엔티티를 타고 들어가라는 표시입니다.
    boolean existsByMember_MemberIdAndMovieId(Long memberId, Long movieId);
}