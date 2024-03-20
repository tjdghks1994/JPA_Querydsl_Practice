package com.puj.domain.comment;

import com.puj.domain.board.Board;
import com.puj.domain.board.BoardType;
import com.puj.domain.comment.repository.dto.SearchCommentResp;
import com.puj.domain.comment.service.CommentService;
import com.puj.domain.member.Member;
import com.puj.domain.member.MemberRole;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService service;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("게시글의 댓글 목록 조회 테스트")
    void searchCommentListTest() {
        Member member = Member.builder()
                .email("hello@gmail.com")
                .pwd(UUID.randomUUID().toString())
                .role(MemberRole.NORMAL)
                .nickname("hardy")
                .oAuthKey(null)
                .build();
        Board board = Board.builder()
                .boardTitle("test")
                .boardContent("hello world!")
                .boardType(BoardType.NORMAL)
                .parentBoard(null)
                .member(member)
                .build();
        Comment comment1 = Comment.builder()
                .content("hihi")
                .member(member)
                .board(board)
                .parentComment(null)
                .build();
        Comment comment2 = Comment.builder()
                .content("JPA는 어려워~")
                .member(member)
                .board(board)
                .parentComment(null)
                .build();
        em.persist(member);
        em.persist(board);
        em.persist(comment1);
        em.persist(comment2);

        List<Comment> commentList = service.searchCommentList(board.getId());

        Assertions.assertThat(commentList.size()).isEqualTo(2);
    }
}