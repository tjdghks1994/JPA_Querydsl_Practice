package com.puj.domain.board;

import com.puj.domain.base.BaseTimeEntity;
import com.puj.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Table(name = "BOARD_TB")
@ToString(exclude = "member")
@NoArgsConstructor(access = PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "board_title")
    private String boardTitle;      // 제목

    @Lob
    @Column(name = "board_content")
    private String boardContent;    // 내용

    @Column(name = "view_cnt")
    private int viewCnt;            // 조회수

    @Column(name = "board_type")
    @Enumerated(value = STRING)
    private BoardType boardType;    // 게시판 타입

    @Column(name = "delete_yn")
    private char deleteYN;          // 삭제 여부

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          // 회원

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_board_id")
    private Board parentBoard;      // 상위 게시글

    @OneToMany(mappedBy = "parentBoard")
    private List<Board> childrenBoard = new ArrayList<>();  // 답글

    @Builder
    public Board(String boardTitle, String boardContent, int viewCnt, BoardType boardType,
                 char deleteYN, Member member, Board parentBoard) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.viewCnt = viewCnt;
        this.boardType = boardType;
        this.deleteYN = deleteYN;
        this.member = member;
        this.parentBoard = parentBoard;
    }
}
