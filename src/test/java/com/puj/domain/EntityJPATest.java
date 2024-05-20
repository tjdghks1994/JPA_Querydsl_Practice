package com.puj.domain;

import com.puj.domain.attachfile.AttachFile;
import com.puj.domain.board.Board;
import com.puj.domain.board.BoardType;
import com.puj.domain.comment.Comment;
import com.puj.domain.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class EntityJPATest {

    @PersistenceContext
    EntityManager em;

    @Test
    void memberEntityJPATest() {
        Member member1 = Member.builder()
                .email("test@naver.com")
                .pwd("askdfeizxoci23lrn13lksl")
                .nickname("하디")
                .build();

        Member member2 = Member.builder()
                .email("test2@naver.com")
                .pwd("a2o3mks2kacxkfn43")
                .nickname("아누")
                .build();

        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        Member findMember1 = em.find(Member.class, member1.getId());
        Member findMember2 = em.find(Member.class, member2.getId());

        assertThat(findMember1.getNickname()).isEqualTo("하디");
        assertThat(findMember1.getEmail()).isEqualTo("test@naver.com");
        assertThat(findMember2.getNickname()).isEqualTo("아누");
        assertThat(findMember2.getEmail()).isEqualTo("test2@naver.com");
        assertThat(findMember1).isNotEqualTo(findMember2);
    }

    @Test
    void boardEntityJPATest() {
        Member member = Member.builder()
                .email("test@naver.com")
                .pwd("askdfeizxoci23lrn13lksl")
                .nickname("하디")
                .build();

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

        Board newBoard = Board.builder()
                .boardTitle("게시글 제목")
                .boardContent("게시글 내용은 무수히 많은 문자열이 작성될 수 있습니다.")
                .boardType(BoardType.NORMAL)
                .member(findMember)
                .deleteYN("Y")
                .parentBoard(null)
                .build();

        em.persist(newBoard);

        em.flush();
        em.clear();

        Board findBoard = em.find(Board.class, newBoard.getId());

        assertThat(findBoard.getId()).isEqualTo(newBoard.getId());
        assertThat(findBoard.getDeleteYN()).isEqualTo("Y");
    }

    @Test
    void commentEntityJPATest() {
        Member member = Member.builder()
                .email("test@naver.com")
                .pwd("askdfeizxoci23lrn13lksl")
                .nickname("하디")
                .build();
        Board newBoard = Board.builder()
                .boardTitle("게시글 제목")
                .boardContent("게시글 내용은 무수히 많은 문자열이 작성될 수 있습니다.")
                .boardType(BoardType.NORMAL)
                .member(member)
                .deleteYN("Y")
                .parentBoard(null)
                .build();

        em.persist(member);
        em.persist(newBoard);

        Comment newComment = Comment.builder()
                .content("댓글 내용")
                .member(member)
                .board(newBoard)
                .parentComment(null)
                .build();

        em.persist(newComment);
        em.flush();
        em.clear();

        Comment findComment = em.find(Comment.class, newComment.getId());

        assertThat(findComment.getId()).isEqualTo(newComment.getId());
        assertThat(findComment.getDeleteYN()).isEqualTo("N");
        assertThat(findComment.getMember().getNickname()).isEqualTo("하디");
    }

    @Test
    void attachFileEntityJPATest() {
        Member member = Member.builder()
                .email("test@naver.com")
                .pwd("askdfeizxoci23lrn13lksl")
                .nickname("하디")
                .build();
        Board newBoard = Board.builder()
                .boardTitle("게시글 제목")
                .boardContent("게시글 내용은 무수히 많은 문자열이 작성될 수 있습니다.")
                .boardType(BoardType.NORMAL)
                .member(member)
                .deleteYN("Y")
                .parentBoard(null)
                .build();
        em.persist(member);
        em.persist(newBoard);

        em.flush();
        em.clear();

        AttachFile newFile = AttachFile.builder()
                .attachPath("/Users/parksunghwan/uploadFile/")
                .saveFilename(UUID.randomUUID().toString())
                .originFilename("파일입니다")
                .attachExtension(".txt")
                .imageYN("N")
                .board(newBoard)
                .build();

        em.persist(newFile);

        AttachFile findFile = em.find(AttachFile.class, newFile.getId());

        assertThat(findFile.getId()).isEqualTo(newFile.getId());
        assertThat(findFile.getAttachExtension()).isEqualTo(newFile.getAttachExtension());
        assertThat(findFile.getBoard().getId()).isEqualTo(newBoard.getId());

    }
}
