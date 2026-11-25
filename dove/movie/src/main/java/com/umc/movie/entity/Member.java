package com.umc.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter // 모든 필드의 getter 메서드를 자동을 만들어줌
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 protected로 자동 생성해줌
public class Member extends BaseTimeEntity {

    @Id // PK이다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 이 값은 DB가 만듦
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email", nullable = false, unique = true, length = 254) // DB와 자바의 컬럼명이 같으면 name지정 안해줘도 됨
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
