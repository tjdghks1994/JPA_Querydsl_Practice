package com.puj.domain.member.service;

import com.puj.domain.member.Member;
import com.puj.domain.member.exception.InvalidMemberException;
import com.puj.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;

    // 이메일로 사용자 조회
    public Member searchMemberByEmail(String email) {
        Member findMember = repository.findByEmail(email)
                .orElseThrow(() -> new InvalidMemberException("존재하지 않는 사용자 입니다."));

        return findMember;
    }
}
