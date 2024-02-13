package com.puj.domain.member;

import com.puj.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@Table(name = "MEMBER_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_email")
    private String email;

    @Column(name = "member_pwd")
    private String pwd;

    @Column(name = "member_nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole role;

    @Column(name = "member_key")
    private String oAuthKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private MemberStatus status;

    @Builder
    private Member(String email, String nickname, MemberRole role, String oAuthKey, MemberStatus status) {
        // 검증 필요

        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.oAuthKey = oAuthKey;
        this.status = status;
    }

}
