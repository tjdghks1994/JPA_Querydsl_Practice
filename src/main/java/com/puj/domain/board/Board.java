package com.puj.domain.board;

import com.puj.domain.base.BaseTimeEntity;
import com.puj.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault(value = "0")
    private int viewCnt;            // 조회수

    @Column(name = "board_type")
    @Enumerated(value = STRING)
    private BoardType boardType;    // 게시판 타입

    @Column(name = "delete_yn")
    @ColumnDefault(value = "N")
    private String deleteYN;    // 삭제 여부

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          // 회원

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_board_id")
    private Board parentBoard;      // 상위 게시글

    @OneToMany(mappedBy = "parentBoard")
    private List<Board> childrenBoard = new ArrayList<>();  // 답글

    @PrePersist
    protected void fieldDefaultValueSetting() {
        this.viewCnt = this.viewCnt == 0 ? 0 : this.viewCnt;
        this.deleteYN = this.deleteYN == null ? "N" : this.deleteYN;
    }

    @Builder
    public Board(String boardTitle, String boardContent, int viewCnt, BoardType boardType,
                 String deleteYN, Member member, Board parentBoard) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.viewCnt = viewCnt;
        this.boardType = boardType;
        this.deleteYN = deleteYN;
        this.member = member;
        this.parentBoard = parentBoard;
    }
    // 게시글 제목, 내용 변경
    public void changeBoardInfo(String boardTitle, String boardContent) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
