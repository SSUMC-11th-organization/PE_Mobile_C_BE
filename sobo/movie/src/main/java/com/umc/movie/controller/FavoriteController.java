// src/main/java/com/umc/movie/controller/FavoriteController.java
package com.umc.movie.controller;

import com.umc.movie.config.SwaggerConfig;
import com.umc.movie.dto.ApiResponse;
import com.umc.movie.dto.CreateFavoriteRequest;
import com.umc.movie.dto.FavoriteResponse;
import com.umc.movie.dto.FavoriteStatusResponse;
import com.umc.movie.interceptor.AuthInterceptor;
import com.umc.movie.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "즐겨찾기", description = "내 즐겨찾기 등록·조회·해제 API")
@SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "즐겨찾기 등록", description = "로그인한 회원이 TMDB 영화를 즐겨찾기에 추가합니다. JWT 토큰이 필요합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201", description = "즐겨찾기 등록 성공 (COMMON201)",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON201",
                              "message": "즐겨찾기를 등록했습니다.",
                              "result": { "favoriteId": 1, "movieId": 550, "createdAt": "2026-09-13T21:00:00" }
                            }
                            """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "입력값 검증 실패 (COMMON4001)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "COMMON4001", value = """
                                    {"isSuccess": false, "code": "COMMON4001", "message": "요청 필드의 검증 조건을 확인해주세요.", "result": null}
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "인증 실패: 토큰 누락 / 유효하지 않은 토큰 / 만료된 토큰 (AUTH401_2, AUTH401_3, AUTH401_4)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "AUTH401_2", value = """
                                    {"isSuccess": false, "code": "AUTH401_2", "message": "로그인이 필요한 요청입니다.", "result": null}
                                    """),
                            @ExampleObject(name = "AUTH401_3", value = """
                                    {"isSuccess": false, "code": "AUTH401_3", "message": "유효하지 않은 토큰입니다.", "result": null}
                                    """),
                            @ExampleObject(name = "AUTH401_4", value = """
                                    {"isSuccess": false, "code": "AUTH401_4", "message": "토큰이 만료되었습니다. 다시 로그인해주세요.", "result": null}
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "해당 회원 없음 (MEMBER404_1)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "MEMBER404_1", value = """
                                    {"isSuccess": false, "code": "MEMBER404_1", "message": "해당 회원을 찾을 수 없습니다.", "result": null}
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409", description = "이미 즐겨찾기한 영화 (FAVORITE409_1)",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "FAVORITE409_1",
                              "message": "이미 이 영화를 즐겨찾기하셨습니다.",
                              "result": null
                            }
                            """)))
    })
    @PostMapping("/favorites")
    public ResponseEntity<ApiResponse<FavoriteResponse>> create(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @RequestBody @Valid CreateFavoriteRequest request
    ) {
        FavoriteResponse response = favoriteService.create(memberId, request);
        return ResponseEntity.status(201)
                .body(ApiResponse.created("즐겨찾기를 등록했습니다.", response));
    }

    @Operation(summary = "내 즐겨찾기 조회", description = "로그인한 회원의 즐겨찾기 목록을 조회합니다. 등록 내역이 없으면 빈 목록을 반환하며 JWT 토큰이 필요합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "내 즐겨찾기 조회 성공 (COMMON200)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "인증 실패: 토큰 누락 / 유효하지 않은 토큰 / 만료된 토큰 (AUTH401_2, AUTH401_3, AUTH401_4)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "AUTH401_2", value = """
                                    {"isSuccess": false, "code": "AUTH401_2", "message": "로그인이 필요한 요청입니다.", "result": null}
                                    """),
                            @ExampleObject(name = "AUTH401_3", value = """
                                    {"isSuccess": false, "code": "AUTH401_3", "message": "유효하지 않은 토큰입니다.", "result": null}
                                    """),
                            @ExampleObject(name = "AUTH401_4", value = """
                                    {"isSuccess": false, "code": "AUTH401_4", "message": "토큰이 만료되었습니다. 다시 로그인해주세요.", "result": null}
                                    """)
                    }))
    })
    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getMyFavorites(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId
    ) {
        List<FavoriteResponse> response = favoriteService.getMyFavorites(memberId);
        return ResponseEntity.ok(ApiResponse.ok("내 즐겨찾기 목록을 조회했습니다.", response));
    }

    @Operation(summary = "즐겨찾기 여부 조회", description = "로그인한 회원이 해당 영화를 즐겨찾기했는지 조회합니다. 미등록 영화는 200과 isFavorite=false를 반환하며 JWT 토큰이 필요합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "즐겨찾기 여부 조회 성공 (COMMON200)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "인증 실패: 토큰 누락 / 유효하지 않은 토큰 / 만료된 토큰 (AUTH401_2, AUTH401_3, AUTH401_4)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "AUTH401_2", value = """
                                    {"isSuccess": false, "code": "AUTH401_2", "message": "로그인이 필요한 요청입니다.", "result": null}
                                    """),
                            @ExampleObject(name = "AUTH401_3", value = """
                                    {"isSuccess": false, "code": "AUTH401_3", "message": "유효하지 않은 토큰입니다.", "result": null}
                                    """),
                            @ExampleObject(name = "AUTH401_4", value = """
                                    {"isSuccess": false, "code": "AUTH401_4", "message": "토큰이 만료되었습니다. 다시 로그인해주세요.", "result": null}
                                    """)
                    }))
    })
    @GetMapping("/favorites/{movieId}")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> getStatus(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long movieId
    ) {
        FavoriteStatusResponse response = favoriteService.getStatus(memberId, movieId);
        return ResponseEntity.ok(ApiResponse.ok("즐겨찾기 여부를 조회했습니다.", response));
    }

    @Operation(summary = "즐겨찾기 해제", description = "로그인한 회원의 즐겨찾기에서 해당 영화를 제거합니다. JWT 토큰이 필요합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "즐겨찾기 해제 성공 (COMMON200)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "인증 실패: 토큰 누락 / 유효하지 않은 토큰 / 만료된 토큰 (AUTH401_2, AUTH401_3, AUTH401_4)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "AUTH401_2", value = """
                                    {"isSuccess": false, "code": "AUTH401_2", "message": "로그인이 필요한 요청입니다.", "result": null}
                                    """),
                            @ExampleObject(name = "AUTH401_3", value = """
                                    {"isSuccess": false, "code": "AUTH401_3", "message": "유효하지 않은 토큰입니다.", "result": null}
                                    """),
                            @ExampleObject(name = "AUTH401_4", value = """
                                    {"isSuccess": false, "code": "AUTH401_4", "message": "토큰이 만료되었습니다. 다시 로그인해주세요.", "result": null}
                                    """)
                    })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "즐겨찾기하지 않은 영화 (FAVORITE404_1)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "FAVORITE404_1", value = """
                                    {"isSuccess": false, "code": "FAVORITE404_1", "message": "즐겨찾기한 영화를 찾을 수 없습니다.", "result": null}
                                    """)
                    }))
    })
    @DeleteMapping("/favorites/{movieId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @PathVariable Long movieId
    ) {
        favoriteService.delete(memberId, movieId);
        return ResponseEntity.ok(ApiResponse.ok("즐겨찾기를 해제했습니다.", null));
    }
}
