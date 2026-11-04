package com.example.movie.controller;

import com.example.movie.dto.EmailCheckResponse;
import com.example.movie.dto.NicknameCheckResponse;
import com.example.movie.exception.InvalidEmailException;
import com.example.movie.exception.InvalidNicknameException;
import com.example.movie.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/nickname/{nickname}")
    public NicknameCheckResponse checkNickname(@PathVariable String nickname) {
        if (nickname.length() > 12 || nickname.length() < 2) {
            throw new InvalidNicknameException("닉네임은 최소 2자에서 최대 12자까지 설정 가능합니다.");
        }
        return memberService.checkNickname(nickname);
    }

    @GetMapping("/email/{email}")
    public EmailCheckResponse checkEmail(@PathVariable String email) {
        if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {                         // 이메일 형식이 맞는지 확인하는 조건
            throw new InvalidEmailException("이메일 형식이 유효하지 않습니다.");
        }
        return memberService.checkEmail(email);
    }
}
