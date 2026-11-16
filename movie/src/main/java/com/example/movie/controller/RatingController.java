package com.example.movie.controller;

import com.example.movie.annotation.LoginMember;
import com.example.movie.config.SwaggerConfig;
import com.example.movie.dto.ApiResponse;
import com.example.movie.dto.CreateRatingRequest;
import com.example.movie.dto.RatingResponse;
import com.example.movie.dto.UpdateRatingRequest;
import com.example.movie.interceptor.AuthInterceptor;
import com.example.movie.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "평점", description = "평점 등록/수정/삭제, 유저별 평점 조회 API")
public class RatingController {

    private final RatingService ratingService;

    @Operation(
            summary = "평점 조회",
            description = "특정 회원이 남긴 평점을 조회합니다. 토큰이 없어도 호출할 수 있는 공개 API입니다."
    )
    // 5주차에 만든 유저별 평점 조회입니다. 공개 API라 토큰을 받지 않습니다.
    @GetMapping("/members/{memberId}/ratings")
    public List<RatingResponse> getMemberRatings(@PathVariable Long memberId) {
        return ratingService.getRatingsByMember(memberId);
    }

    @Operation(
            summary = "평점 등록",
            description = "토큰의 주인인 회원이 TMDB 영화에 점수와 코멘트를 등록합니다."
    )
    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201", description = "평점 등록 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
              "isSuccess": true,
              "code": "COMMON201",
              "message": "평점을 등록했습니다.",
              "result": { "ratingId": 1, "movieId": 550, "score": 5, "comment": "...", "createdAt": "2026-11-16T17:00:00", "updatedAt": "2026-11-16T17:00:00" }
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
                    responseCode = "409", description = "이미 등록된 평점",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            { 
             "isSuccess": false, 
             "code": "RATING409_1", 
             "message": "이미 이 영화에 평점을 남기셨습니다.", 
             "result": null 
            }
            """)))
    })
    // 경로에서 /members/{memberId}가 사라지고 memberId는 인터셉터가 담아둔 값을 씁니다
    @PostMapping("/ratings")
    public ResponseEntity<ApiResponse<RatingResponse>> create(
//            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @LoginMember Long memberId,
            @RequestBody @Valid CreateRatingRequest request
    ) {
        RatingResponse response = ratingService.create(memberId, request);
        return ResponseEntity.status(201)
                .body(ApiResponse.created("평점을 등록했습니다.", response));
    }

    @Operation(
            summary = "평점 수정",
            description = "토큰의 주인인 회원이 자신의 평점의 점수와 코멘트를 수정합니다."
    )
    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "평점 수정 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                               "isSuccess": true,
                               "code": "COMMON200",
                               "message": "평점을 수정했습니다.",
                               "result": {
                                 "ratingId": 12,
                                 "movieId": 550,
                                 "score": 5,
                                 "comment": "최고의 영화!",
                                 "createdAt": "2026-09-06T12:14:03.000Z",
                                 "updatedAt": "2026-09-06T12:14:03.000Z"
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
                    responseCode = "403", description = "다른 회원이 작성한 평점",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                               "isSuccess": false,
                               "code": "RATING403_1",
                               "message": "본인이 작성한 평점만 수정하거나 삭제할 수 있습니다.",
                               "result": null
                             }
            """)))
    })
    // 경로에서도 /{memberId}가 사라지고 인터셉터가 담아둔 값을 씁니다
    @PatchMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<RatingResponse>> update(
//            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @LoginMember Long memberId,
            @PathVariable Long ratingId,
            @RequestBody @Valid UpdateRatingRequest request
    ) {
        RatingResponse response = ratingService.update(memberId, ratingId, request);
        return ResponseEntity.ok(ApiResponse.ok("평점을 수정했습니다.", response));
    }

    @Operation(
            summary = "평점 삭제",
            description = "토큰의 주인인 회원이 자신의 평점을 삭제합니다."
    )
    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "평점 삭제 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                                                "isSuccess": true,
                                                "code": "COMMON200",
                                                "message": "평점을 삭제했습니다.",
                                                "result": null
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
                    responseCode = "403", description = "다른 회원이 작성한 평점 삭제",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "RATING403_1",
                              "message": "본인이 작성한 평점만 수정하거나 삭제할 수 있습니다.",
                              "result": null
                            }
            """)))
    })
    @DeleteMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<Void>> delete(
//            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @LoginMember Long memberId,
            @PathVariable Long ratingId
    ) {
        ratingService.delete(memberId, ratingId);
        return ResponseEntity.ok(ApiResponse.ok("평점을 삭제했습니다.", null));
    }
}