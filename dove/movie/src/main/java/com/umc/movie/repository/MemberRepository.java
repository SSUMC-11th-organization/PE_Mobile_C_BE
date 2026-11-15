package com.umc.movie.repository;

import com.umc.movie.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JPA를 상속받아 사용할 때는 interface 사용
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email); // null이 반환될 수도 있기에 Optional로 감쌈
    boolean existsByNicknameAndMemberIdNot(String nickname, Long memberId); // memberId인 member는 빼고 검사
}