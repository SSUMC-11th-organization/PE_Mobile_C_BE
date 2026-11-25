package com.example.movie.controller;

import com.example.movie.config.SwaggerConfig;
import com.example.movie.dto.*;
import com.example.movie.interceptor.AuthInterceptor;
import com.example.movie.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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


    @Operation(
            summary = "닉네임 중복체크",
            description = "닉네임 중복을 체크합니다."
    )
    @GetMapping("/nickname/{nickname}")
    public NicknameCheckResponse checkNickname(@PathVariable String nickname) {
        return memberService.checkNickname(nickname);
    }

    @Operation(
            summary = "이메일 중복체크",
            description = "이메일 중복을 체크합니다."
    )
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

    @Operation(
            summary = "내 정보 조회",
            description = "내 ID, 이메일, 닉네임을 조회합니다."
    )
    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "memberId 와 password 는 담기지 않습니다.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                               "isSuccess": true,
                               "code": "COMMON200",
                               "message": "회원 정보를 조회했습니다.",
                               "result": {
                                 "email": "movie@umc.com",
                                 "nickname": "영화광",
                                 "createdAt": "2026-09-06T12:14:03.000Z"
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
                    responseCode = "404", description = "해당 회원을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                               "isSuccess": false,
                               "code": "MEMBER404_1",
                               "message": "해당 회원을 찾을 수 없습니다.",
                               "result": null
                             }
            """)))
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberInfoResponse>> getMyInfo(
            // 인터셉터가 request.setAttribute("memberId", ...)로 담아둔 값을 꺼냅니다
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId
    ) {
        MemberInfoResponse response = memberService.getMyInfo(memberId);
        return ResponseEntity.ok(ApiResponse.ok("회원 정보를 조회했습니다.", response));
    }

    @Operation(
            summary = "내 정보 수정",
            description = "내 회원 정보를 수정합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "회원 정보를 수정했습니다.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                                                 "isSuccess": true,
                                                 "code": "COMMON200",
                                                 "message": "회원 정보를 수정했습니다.",
                                                 "result": {
                                                   "email": "movie@umc.com",
                                                   "nickname": "영화광",
                                                   "createdAt": "2026-09-06T12:14:03.000Z"
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
                    responseCode = "404", description = "해당 회원을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                               "isSuccess": false,
                               "code": "MEMBER404_1",
                               "message": "해당 회원을 찾을 수 없습니다.",
                               "result": null
                             }
            """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409", description = "이미 사용 중인 닉네임입니다.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                                                "isSuccess": false,
                                                "code": "MEMBER409_1",
                                                "message": "이미 사용 중인 닉네임입니다.",
                                                "result": null
                                              }
            """)))
    })
    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<MemberInfoResponse>> updateMyInfo(
            @RequestAttribute(AuthInterceptor.MEMBER_ID) Long memberId,
            @RequestBody @Valid MemberUpdateRequest request
    ) {
        MemberInfoResponse response = memberService.updateMyInfo(memberId, request);
        return ResponseEntity.ok(ApiResponse.ok("회원 정보를 수정했습니다.", response));
    }
}
