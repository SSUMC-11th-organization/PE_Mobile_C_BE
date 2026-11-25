// src/main/java/com/umc/movie/controller/MemberController.java
package com.umc.movie.controller;

import com.umc.movie.config.SwaggerConfig;
import com.umc.movie.dto.*;
import com.umc.movie.interceptor.AuthInterceptor;
import com.umc.movie.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "회원", description = "회원가입과 내 정보 API")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/nickname/{nickname}")
    public NicknameCheckResponse checkNickname(@PathVariable String nickname) {
        return memberService.checkNickname(nickname);
    }

    @GetMapping("/email/{email}")
    public EmailCheckResponse checkEmail(@PathVariable String email) {
        return memberService.checkEmail(email);
    }

    @Operation(
            summary = "회원가입",
            description = "이메일, 비밀번호, 닉네임으로 회원을 등록합니다. 토큰이 없어도 호출할 수 있는 공개 API입니다."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        MemberResponse response = memberService.signUp(request);
        return ResponseEntity.status(201)
                .body(ApiResponse.created("회원가입에 성공했습니다.", response));
    }

    @Operation(summary = "내 정보 조회", description = "토큰의 주인인 회원의 정보를 조회합니다. JWT 토큰이 필요합니다.")
    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "내 정보 조회 성공 (COMMON200)"),
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
                    }))
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberInfoResponse>> getMyInfo(
            // 인터셉터가 request.setAttribute("memberId", ...)로 담아둔 값을 꺼냅니다
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId
    ) {
        MemberInfoResponse response = memberService.getMyInfo(memberId);
        return ResponseEntity.ok(ApiResponse.ok("회원 정보를 조회했습니다.", response));
    }

    // src/main/java/com/umc/movie/controller/MemberController.java 에 아래 메서드를 추가합니다
    @Operation(summary = "내 정보 수정", description = "로그인한 회원의 닉네임을 수정합니다. JWT 토큰이 필요합니다.")
    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "내 정보 수정 성공 (COMMON200)"),
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
                    responseCode = "409", description = "닉네임 중복 (MEMBER409_1)",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "MEMBER409_1", value = """
                                    {"isSuccess": false, "code": "MEMBER409_1", "message": "이미 사용 중인 닉네임입니다.", "result": null}
                                    """)
                    }))
    })
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<MemberInfoResponse>> updateMyInfo(
            @Parameter(hidden = true) @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @RequestBody @Valid MemberUpdateRequest request
    ) {
        MemberInfoResponse response = memberService.updateMyInfo(memberId, request);
        return ResponseEntity.ok(ApiResponse.ok("회원 정보를 수정했습니다.", response));
    }
}
