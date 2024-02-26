package com.puj.domain.board.service;

import com.puj.domain.board.BoardType;
import com.puj.domain.board.exception.InvalidBoardException;
import com.puj.domain.board.repository.BoardRepository;
import com.puj.domain.board.service.dto.CreateBoardReq;
import com.puj.domain.board.service.dto.SearchBoardResp;
import com.puj.domain.member.Member;
import com.puj.domain.member.MemberRole;
import com.puj.domain.member.MemberStatus;
import com.puj.domain.member.exception.InvalidMemberException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    EntityManager em;

    @Test
    void createTest() {
        Member member = Member.builder()
                .email("test@gmail.com")
                .pwd(UUID.randomUUID().toString())
                .nickname("하디")
                .role(MemberRole.NORMAL)
                .status(MemberStatus.ENABLE)
                .oAuthKey(null)
                .build();
        CreateBoardReq req = CreateBoardReq.builder()
                .boardTitle("게시글 작성")
                .boardContent("게시글 내용~~")
                .boardType(BoardType.NORMAL)
                .writer("test@gmail.com")
                .build();

        em.persist(member);
        Long boardId = boardService.createBoard(req);

        assertThat(boardId).isGreaterThan(0L);
    }

    @Test
    void createFailTest() {
        Member member = Member.builder()
                .email("test22@gmail.com")
                .pwd(UUID.randomUUID().toString())
                .nickname("하디")
                .role(MemberRole.NORMAL)
                .status(MemberStatus.ENABLE)
                .oAuthKey(null)
                .build();

        CreateBoardReq req = CreateBoardReq.builder()
                .boardTitle("게시글 작성")
                .boardContent("게시글 내용~~")
                .boardType(BoardType.NORMAL)
                .writer("test@gmail.com")
                .build();

        em.persist(member);
        assertThatThrownBy(() -> boardService.createBoard(req))
                .isInstanceOf(InvalidMemberException.class);
    }

    @Test
    @DisplayName("게시글 조회 성공 테스트")
    void searchBoardTest() {
        // given
        Member member = Member.builder()
                .email("test@gmail.com")
                .pwd(UUID.randomUUID().toString())
                .nickname("하디")
                .role(MemberRole.NORMAL)
                .status(MemberStatus.ENABLE)
                .oAuthKey(null)
                .build();
        CreateBoardReq req = CreateBoardReq.builder()
                .boardTitle("게시글 작성")
                .boardContent("게시글 내용~~")
                .boardType(BoardType.NORMAL)
                .writer("test@gmail.com")
                .build();
        em.persist(member);
        Long boardId = boardService.createBoard(req);

        // when
        SearchBoardResp findBoardInfo = boardService.searchBoard(boardId);

        // then
        assertThat(findBoardInfo.getBoardId()).isEqualTo(boardId);
        assertThat(findBoardInfo.getBoardTitle()).isEqualTo(req.getBoardTitle());
        assertThat(findBoardInfo.getWriter()).isEqualTo(member.getNickname());
        assertThat(findBoardInfo.getAttachList().size()).isEqualTo(0);
        assertThat(findBoardInfo.getCommentList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회하는 실패 테스트")
    void searchBoardFailTest() {
        Long boardId = 0L;

        assertThatThrownBy(() -> boardService.searchBoard(boardId))
                .isInstanceOf(InvalidBoardException.class);
    }
}