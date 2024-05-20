package com.puj.domain.member;

import com.puj.domain.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault(value = "NORMAL")
    private MemberRole role;

    @Column(name = "member_key")
    private String oAuthKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    @ColumnDefault(value = "ENABLE")
    private MemberStatus status;

    @PrePersist
    protected void fieldDefaultValueSetting() {
        this.role = this.role == null ? MemberRole.NORMAL : this.role;
        this.status = this.status == null ? MemberStatus.ENABLE : this.status;
    }

    @Builder
    private Member(String email, String pwd, String nickname, MemberRole role, String oAuthKey, MemberStatus status) {
        this.email = email;
        this.pwd = pwd;
        this.nickname = nickname;
        this.role = role;
        this.oAuthKey = oAuthKey;
        this.status = status;
    }

    // 비밀번호 변경
    public void changePwd(String pwd) {
        this.pwd = pwd;
    }
    // 닉네임 변경
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

}
