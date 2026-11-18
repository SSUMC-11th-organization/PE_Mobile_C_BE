package com.example.movie.service;

import com.example.movie.dto.CreateRatingRequest;
import com.example.movie.dto.UpdateRatingRequest;
import com.example.movie.entity.Member;
import com.example.movie.entity.Rating;
import com.example.movie.exception.ApiException;
import com.example.movie.repository.MemberRepository;
import com.example.movie.repository.RatingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository; // 진짜 DB 대신 들어가는 가짜 Repository

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RatingService ratingService; // 위의 가짜 두 개를 주입받은 진짜 Service

    private final Member member = Member.builder()
            .email("challenger@umc.com")
            .password("암호화된비밀번호")
            .nickname("영화조아")
            .build();

    @Test
    @DisplayName("같은 영화에 평점을 중복 등록하면 RATING409_1 예외를 던지고 저장하지 않는다")
    void createRatingWhenDuplicated() {
        // given: 같은 회원이 같은 영화에 이미 평점 등록을 해 둔 상황
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(ratingRepository.existsByMember_MemberIdAndMovieId(1L, 550L)).willReturn(true);

        // when & then: 예외가 나고, 저장은 시도조차 하지 않는다
        CreateRatingRequest request = new CreateRatingRequest(550L, 5, "최고의 영화!");
        assertThatThrownBy(() -> ratingService.create(1L, request))
                .isInstanceOf(ApiException.class)
                .extracting("code").isEqualTo("RATING409_1");

        verify(ratingRepository, never()).save(any());
    }

    @Test
    @DisplayName("남의 평점을 수정하려 하면 RATING403_1 예외를 던지고 저장하지 않는다")
    void updateRatingWhenOther() {
        // given: 다른 회원이 평점을 등록해둔 상황
        ReflectionTestUtils.setField(member, "memberId", 1L);
        Rating rating = Rating.builder()
                .member(member).movieId(550L).score(5).comment("원래 코멘트").build();

        given(ratingRepository.findById(1L)).willReturn(Optional.of(rating));

        // when&then: 다른 회원의 평점을 내가 수정하려 할 때 예외가 나고, 수정은 되지 않는다
        UpdateRatingRequest request = new UpdateRatingRequest(1, "바꾸려는 코멘트");
        assertThatThrownBy(() -> ratingService.update(2L, 1L, request))
                .isInstanceOf(ApiException.class)
                .extracting("code").isEqualTo("RATING403_1");

        assertThat(rating.getScore()).isEqualTo(5);
        assertThat(rating.getComment()).isEqualTo("원래 코멘트");
    }
}
