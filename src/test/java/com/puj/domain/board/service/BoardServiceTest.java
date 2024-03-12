package com.puj.domain.board.service;

import com.puj.domain.board.Board;
import com.puj.domain.board.BoardType;
import com.puj.domain.board.exception.InvalidBoardException;
import com.puj.domain.board.service.dto.CreateBoardReq;
import com.puj.domain.board.service.dto.ModifyBoardReq;
import com.puj.domain.board.service.dto.SearchBoardResp;
import com.puj.domain.member.Member;
import com.puj.domain.member.MemberRole;
import com.puj.domain.member.MemberStatus;
import com.puj.domain.member.exception.RequiredMemberException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    EntityManager em;

    private Member member1;
    private Member member2;
    private CreateBoardReq boardReq1;
    private CreateBoardReq boardReq2;

    @BeforeEach
    void initEntity() {
        member1 = Member.builder()
                .email("test@gmail.com")
                .pwd(UUID.randomUUID().toString())
                .nickname("하디")
                .role(MemberRole.NORMAL)
                .status(MemberStatus.ENABLE)
                .oAuthKey(null)
                .build();
        member2 = Member.builder()
                .email("test22@gmail.com")
                .pwd(UUID.randomUUID().toString())
                .nickname("하디")
                .role(MemberRole.NORMAL)
                .status(MemberStatus.ENABLE)
                .oAuthKey(null)
                .build();
        boardReq1 = CreateBoardReq.builder()
                .boardTitle("게시글 작성")
                .boardContent("게시글 내용~~")
                .boardType(BoardType.NORMAL)
                .writer("test@gmail.com")
                .build();
        boardReq2 = CreateBoardReq.builder()
                .boardTitle("게시글 작성2")
                .boardContent("게시글 내용2~~")
                .boardType(BoardType.NORMAL)
                .writer("test@gmail.com")
                .build();

        em.persist(member1);
        em.persist(member2);
    }

    @Test
    @DisplayName("게시글 저장 성공 테스트")
    void createTest() {
        Long boardId = boardService.createBoard(boardReq1, Optional.ofNullable(member1));
        Long boardId2 = boardService.createBoard(boardReq2, Optional.ofNullable(member2));

        assertThat(boardId).isGreaterThan(0L);
        assertThat(boardId2).isGreaterThan(0L);
    }

    @Test
    @DisplayName("게시글 저장 실패 테스트")
    void createFailTest() {
        assertThatThrownBy(() -> boardService.createBoard(boardReq1, Optional.ofNullable(null)))
                .isInstanceOf(RequiredMemberException.class);
    }

    @Test
    @DisplayName("게시글 조회 성공 테스트")
    void readBoardTest() {
        // given
        Long boardId = boardService.createBoard(boardReq1, Optional.ofNullable(member1));

        // when
        Board findBoardInfo = boardService.readBoard(boardId);

        // then
        assertThat(findBoardInfo.getId()).isEqualTo(boardId);
        assertThat(findBoardInfo.getBoardTitle()).isEqualTo(boardReq1.getBoardTitle());
        assertThat(findBoardInfo.getMember().getNickname()).isEqualTo(member1.getNickname());
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회하는 실패 테스트")
    void readBoardFailTest() {
        Long boardId = 0L;

        assertThatThrownBy(() -> boardService.readBoard(boardId))
                .isInstanceOf(InvalidBoardException.class);
    }

    @Test
    @DisplayName("게시글 수정 성공 테스트")
    void updateBoardTest() {
        Long boardId = boardService.createBoard(boardReq1, Optional.ofNullable(member1));
        ModifyBoardReq modifyBoardReq = ModifyBoardReq.builder()
                .boardId(boardId)
                .boardTitle("수정!")
                .boardContent("내용도 수정~~!")
                .memberEmail(member1.getEmail())
                .build();

        Long updateBoardId = boardService.updateBoard(modifyBoardReq);
        Board findBoard = boardService.readBoard(updateBoardId);

        assertThat(findBoard.getBoardTitle()).isEqualTo(modifyBoardReq.getBoardTitle());
        assertThat(findBoard.getBoardContent()).isEqualTo(modifyBoardReq.getBoardContent());
    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void removeBoard() {
        Long boardId = boardService.createBoard(boardReq1, Optional.of(member1));
        Board findBoard = em.createQuery("select b from Board b where b.id = :boardId", Board.class)
                .setParameter("boardId", boardId)
                .getSingleResult();

        DeleteBoardReq deleteBoardReq = DeleteBoardReq.builder()
                .boardId(boardId)
                .writer(member1.getEmail())
                .build();

        boardService.removeBoard(deleteBoardReq);

        assertThat(findBoard.getDeleteYN()).isEqualTo("Y");
    }
}