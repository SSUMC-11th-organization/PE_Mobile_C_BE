package com.example.movie.controller;

import com.example.movie.dto.CreateMovieRecordRequest;
import com.example.movie.entity.Favorite;
import com.example.movie.entity.Member;
import com.example.movie.repository.FavoriteRepository;
import com.example.movie.repository.MemberRepository;
import com.example.movie.repository.RatingRepository;
import com.example.movie.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest        // 진짜 서버를 띄우듯 모든 Bean을 올립니다
@AutoConfigureMockMvc  // HTTP 요청을 흉내 내는 MockMvc를 준비합니다
public class MovieRecordApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // 객체를 JSON 문자열로 바꿔줍니다

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private Member member;
    private String authorizationHeader;

    @BeforeEach
    void setUp() {
        // 로그인한 회원이 있어야 하므로 회원을 한 명 저장하고 토큰을 직접 발급합니다.
        member = memberRepository.save(Member.builder()
                .email("challenger@umc.com")
                .password("$2a$10$abcdefghijklmnopqrstuv") // 로그인 API를 거치지 않으므로 아무 값이나 괜찮습니다
                .nickname("영화조아")
                .build());

        authorizationHeader = "Bearer " + jwtUtil.createAccessToken(member.getMemberId(), member.getEmail());
    }

    @AfterEach
    void cleanUp() {
        favoriteRepository.deleteAll();
        ratingRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("이미 즐겨찾기한 영화에 addToFavorites: true로 영화 기록을 저장하면 FAVORITE409_1로 실패")
    void create_existedFavorite() throws Exception {
        favoriteRepository.save(Favorite.builder().member(member).movieId(550L).build());
        long ratingCountBefore = ratingRepository.count();

        mockMvc.perform(post("/movie-records")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateMovieRecordRequest(550L, 5, "좋아요",true))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("FAVORITE409_1"));

        assertThat(ratingRepository.count()).isEqualTo(ratingCountBefore);
        assertThat(ratingRepository.existsByMember_MemberIdAndMovieId(member.getMemberId(), 550L)).isFalse();
    }
}
