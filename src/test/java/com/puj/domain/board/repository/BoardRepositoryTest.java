package com.puj.domain.board.repository;

import com.puj.domain.board.Board;
import com.puj.domain.board.BoardType;
import com.puj.domain.member.Member;
import com.puj.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class BoardRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Test
    void saveTest() {
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

        Member saveMember = memberRepository.save(member);
        Board saveBoard = boardRepository.save(newBoard);

        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow(IllegalStateException::new);
        Board findBoard = boardRepository.findById(saveBoard.getId()).orElseThrow(IllegalStateException::new);

        assertThat(saveMember.getId()).isEqualTo(findMember.getId());
        assertThat(saveBoard.getId()).isEqualTo(findBoard.getId());
        assertThatThrownBy(() -> boardRepository.findById(Long.MAX_VALUE).orElseThrow(IllegalStateException::new))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void updateTest() {
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
        Member saveMember = memberRepository.save(member);
        Board saveBoard = boardRepository.save(newBoard);

        // DirtyChecking
        saveMember.changePwd("ecaskeixoqk31413dmcksl3m1l");
        saveMember.changeNickname("울보하디");
        saveBoard.changeBoardInfo("제목 변경", "내용 변경");

        // JPQL 실행 전, 영속성 컨텍스트 플러시 발생함
        Member findMember = em.createQuery("select m from Member m where m.id = :memberId", Member.class)
                .setParameter("memberId", saveMember.getId())
                .getSingleResult();

        Board findBoard = em.createQuery("select b from Board b where b.id = :boardId", Board.class)
                .setParameter("boardId", saveBoard.getId())
                .getSingleResult();

        assertThat(findMember.getNickname()).isEqualTo("울보하디");
        assertThat(findMember.getPwd()).isNotEqualTo("askdfeizxoci23lrn13lksl");
        assertThat(findBoard.getBoardTitle()).isEqualTo("제목 변경");
        assertThat(findBoard.getBoardContent()).isEqualTo("내용 변경");
    }
}