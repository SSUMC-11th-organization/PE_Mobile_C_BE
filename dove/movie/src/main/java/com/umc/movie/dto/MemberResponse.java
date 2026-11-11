// src/main/java/com/umc/movie/dto/MemberResponse.java
package com.umc.movie.dto;

// 엔티티를 그대로 돌려주면 password까지 나가버리므로 필요한 필드만 옮겨 담습니다.
public record MemberResponse(
        Long memberId,
        String email,
        String nickname
) {}
