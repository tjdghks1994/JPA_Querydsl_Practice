package com.puj.domain.member.repository;

import com.puj.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA 사용
public interface MemberRepository extends JpaRepository<Member, Long> {
}
