package com.puj.domain.comment;

import com.puj.domain.base.BaseTimeEntity;
import com.puj.domain.board.Board;
import com.puj.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Table(name = "COMMENT_TB")
@ToString(exclude = {"board", "member"})
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "comment_content")
    private String content;         // 댓글 내용

    @Column(name = "delete_yn")
    @ColumnDefault(value = "N")
    private String deleteYN;    // 삭제 여부

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;            // 게시글

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          // 회원

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;  // 상위 댓글

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childrenComments = new ArrayList<>(); // 대댓글

    @PrePersist
    protected void fieldDefaultValueSetting() {
        this.deleteYN = this.deleteYN == null ? "N" : this.deleteYN;
    }

    @Builder
    public Comment(String content, String deleteYN, Board board, Member member, Comment parentComment) {
        this.content = content;
        this.deleteYN = deleteYN;
        this.board = board;
        this.member = member;
        this.parentComment = parentComment;
    }
}
