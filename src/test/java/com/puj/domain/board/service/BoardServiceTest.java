package com.puj.domain.board.service;

import com.puj.domain.board.BoardType;
import com.puj.domain.board.exception.InvalidBoardException;
import com.puj.domain.attachfile.repository.dto.CreateAttachReq;
import com.puj.domain.board.service.dto.CreateBoardReq;
import com.puj.domain.board.service.dto.SearchBoardResp;
import com.puj.domain.member.Member;
import com.puj.domain.member.MemberRole;
import com.puj.domain.member.MemberStatus;
import com.puj.domain.member.exception.InvalidMemberException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
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
        CreateBoardReq boardReq = CreateBoardReq.builder()
                .boardTitle("게시글 작성")
                .boardContent("게시글 내용~~")
                .boardType(BoardType.NORMAL)
                .writer("test@gmail.com")
                .build();
        CreateAttachReq attachReq = CreateAttachReq.builder()
                .originFilename("텍스트파일")
                .attachExtension(".txt")
                .build();
        CreateAttachReq attachReq2 = CreateAttachReq.builder()
                .originFilename("텍스트파일22")
                .attachExtension(".txt")
                .build();
        CreateAttachReq attachReq3 = CreateAttachReq.builder()
                .originFilename("텍스트파일33333")
                .attachExtension(".txt")
                .build();
        List<CreateAttachReq> attachReqs = Arrays.asList(attachReq, attachReq2, attachReq3);

        CreateBoardReq boardReq2 = CreateBoardReq.builder()
                .boardTitle("게시글 작성2")
                .boardContent("게시글 내용2~~")
                .boardType(BoardType.NORMAL)
                .writer("test@gmail.com")
                .build();

        em.persist(member);
        Long boardId = boardService.createBoard(boardReq, attachReqs);
        Long boardId2 = boardService.createBoard(boardReq2, null);

        assertThat(boardId).isGreaterThan(0L);
        assertThat(boardId2).isGreaterThan(0L);
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

        CreateBoardReq boardReq = CreateBoardReq.builder()
                .boardTitle("게시글 작성")
                .boardContent("게시글 내용~~")
                .boardType(BoardType.NORMAL)
                .writer("test@gmail.com")
                .build();
        CreateAttachReq attachReq = CreateAttachReq.builder()
                .originFilename("텍스트파일")
                .attachExtension(".txt")
                .build();
        List<CreateAttachReq> attachReqs = Arrays.asList(attachReq);

        em.persist(member);
        assertThatThrownBy(() -> boardService.createBoard(boardReq, attachReqs))
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
        CreateBoardReq boardReq = CreateBoardReq.builder()
                .boardTitle("게시글 작성")
                .boardContent("게시글 내용~~")
                .boardType(BoardType.NORMAL)
                .writer("test@gmail.com")
                .build();
        CreateAttachReq attachReq = CreateAttachReq.builder()
                .originFilename("텍스트파일")
                .attachExtension(".txt")
                .build();
        CreateAttachReq attachReq2 = CreateAttachReq.builder()
                .originFilename("텍스트파일22")
                .attachExtension(".txt")
                .build();
        CreateAttachReq attachReq3 = CreateAttachReq.builder()
                .originFilename("텍스트파일33333")
                .attachExtension(".txt")
                .build();
        List<CreateAttachReq> attachReqs = Arrays.asList(attachReq, attachReq2, attachReq3);

        em.persist(member);
        Long boardId = boardService.createBoard(boardReq, attachReqs);

        // when
        SearchBoardResp findBoardInfo = boardService.readBoard(boardId);

        // then
        assertThat(findBoardInfo.getBoardId()).isEqualTo(boardId);
        assertThat(findBoardInfo.getBoardTitle()).isEqualTo(boardReq.getBoardTitle());
        assertThat(findBoardInfo.getWriter()).isEqualTo(member.getNickname());
        assertThat(findBoardInfo.getAttachList().size()).isEqualTo(3);
        assertThat(findBoardInfo.getCommentList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회하는 실패 테스트")
    void searchBoardFailTest() {
        Long boardId = 0L;

        assertThatThrownBy(() -> boardService.readBoard(boardId))
                .isInstanceOf(InvalidBoardException.class);
    }
}