package com.example.movie.controller;

import com.example.movie.config.SwaggerConfig;
import com.example.movie.dto.ApiResponse;
import com.example.movie.dto.CreateMovieRecordRequest;
import com.example.movie.dto.MovieRecordResponse;
import com.example.movie.interceptor.AuthInterceptor;
import com.example.movie.service.MovieRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "영화 기록 저장", description = "평점과 즐겨찾기를 한 트랜잭션으로 저장하는 API")
@SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
public class MovieRecordController {

    private final MovieRecordService movieRecordService;

    @Operation(
            summary = "영화 기록 등록",
            description = "토큰의 주인인 회원이 평점과 즐겨찾기를 한 트랜잭션으로 저장합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201", description = "영화 기록 등록 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                               "isSuccess": true,
                               "code": "COMMON201",
                               "message": "영화 기록을 저장했습니다.",
                               "result": {
                                 "rating": {
                                   "ratingId": 12,
                                   "movieId": 550,
                                   "score": 5,
                                   "comment": "최고의 영화!",
                                   "createdAt": "2026-09-06T12:14:03.000Z",
                                   "updatedAt": "2026-09-06T12:14:03.000Z"
                                 },
                                 "favorite": {
                                   "favoriteId": 3,
                                   "movieId": 550,
                                   "createdAt": "2026-09-06T12:14:03.000Z"
                                 }
                               }
                             }
                            """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "토큰이 없거나 유효하지 않음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH401_2",
                              "message": "로그인이 필요한 요청입니다.",
                              "result": null
                            }
                            """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409", description = "이미 기록한 영화",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                               "isSuccess": false,
                               "code": "RATING409_1",
                               "message": "이미 이 영화에 평점을 남기셨습니다. / 이미 이 영화를 즐겨찾기하셨습니다. (FAVORITE409_1)",
                               "result": null
                             }
                            """)))
    })
    @PostMapping("/movie-records")
    public ResponseEntity<ApiResponse<MovieRecordResponse>> create(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @RequestBody @Valid CreateMovieRecordRequest request
    ) {
        MovieRecordResponse response = movieRecordService.create(memberId, request);
        return ResponseEntity.status(201)
                .body(ApiResponse.created("영화 기록을 저장했습니다.", response));
    }
}
