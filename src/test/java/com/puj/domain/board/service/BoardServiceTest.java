package com.puj.domain.board.service;

import com.puj.domain.attachfile.AttachFile;
import com.puj.domain.board.BoardType;
import com.puj.domain.board.exception.InvalidBoardException;
import com.puj.domain.attachfile.repository.dto.CreateAttachReq;
import com.puj.domain.board.service.dto.CreateBoardReq;
import com.puj.domain.board.service.dto.ModifyBoardReq;
import com.puj.domain.board.service.dto.SearchBoardResp;
import com.puj.domain.member.Member;
import com.puj.domain.member.MemberRole;
import com.puj.domain.member.MemberStatus;
import com.puj.domain.member.exception.InvalidMemberException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

    private Member member1;
    private Member member2;
    private CreateBoardReq boardReq1;
    private CreateBoardReq boardReq2;
    private CreateAttachReq attachReq1;
    private CreateAttachReq attachReq2;
    private CreateAttachReq attachReq3;

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
        attachReq1 = CreateAttachReq.builder()
                .originFilename("텍스트파일")
                .attachExtension(".txt")
                .build();
        attachReq2 = CreateAttachReq.builder()
                .originFilename("텍스트파일22")
                .attachExtension(".txt")
                .build();
        attachReq3 = CreateAttachReq.builder()
                .originFilename("텍스트파일33333")
                .attachExtension(".txt")
                .build();
    }

    @Test
    void createTest() {
        List<CreateAttachReq> attachReqs = Arrays.asList(attachReq1, attachReq2, attachReq3);

        em.persist(member1);
        Long boardId = boardService.createBoard(boardReq1, attachReqs);
        Long boardId2 = boardService.createBoard(boardReq2, null);

        assertThat(boardId).isGreaterThan(0L);
        assertThat(boardId2).isGreaterThan(0L);
    }

    @Test
    void createFailTest() {
        List<CreateAttachReq> attachReqs = Arrays.asList(attachReq1);

        em.persist(member2);
        assertThatThrownBy(() -> boardService.createBoard(boardReq1, attachReqs))
                .isInstanceOf(InvalidMemberException.class);
    }

    @Test
    @DisplayName("게시글 조회 성공 테스트")
    void readBoardTest() {
        // given
        List<CreateAttachReq> attachReqs = Arrays.asList(attachReq1, attachReq2, attachReq3);

        em.persist(member1);
        Long boardId = boardService.createBoard(boardReq1, attachReqs);

        // when
        SearchBoardResp findBoardInfo = boardService.readBoard(boardId);

        // then
        assertThat(findBoardInfo.getBoardId()).isEqualTo(boardId);
        assertThat(findBoardInfo.getBoardTitle()).isEqualTo(boardReq1.getBoardTitle());
        assertThat(findBoardInfo.getWriter()).isEqualTo(member1.getNickname());
        assertThat(findBoardInfo.getAttachList().size()).isEqualTo(3);
        assertThat(findBoardInfo.getCommentList().size()).isEqualTo(0);
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
        List<CreateAttachReq> attachReqs = Arrays.asList(attachReq1, attachReq2, attachReq3);
        em.persist(member1);
        Long boardId = boardService.createBoard(boardReq1, attachReqs);
        List<AttachFile> attachFiles = em.createQuery("select a from AttachFile a where a.board.id = :boardId", AttachFile.class)
                .setParameter("boardId", boardId)
                .getResultList();

        ModifyBoardReq modifyBoardReq = ModifyBoardReq.builder()
                .boardId(boardId)
                .boardTitle("수정!")
                .boardContent("내용도 수정~~!")
                .memberEmail(member1.getEmail())
                .build();
        List<Long> deleteFileIds = Arrays.asList(attachFiles.get(0).getId(), attachFiles.get(1).getId());
        List<CreateAttachReq> newFiles = Arrays.asList(
                CreateAttachReq.builder()
                        .originFilename("newFile")
                        .attachExtension(".txt")
                        .build()
        );

        Long updateBoardId = boardService.updateBoard(modifyBoardReq, deleteFileIds, newFiles);
        em.clear();
        em.close();
        SearchBoardResp findBoard = boardService.readBoard(updateBoardId);

        assertThat(findBoard.getBoardTitle()).isEqualTo(modifyBoardReq.getBoardTitle());
        assertThat(findBoard.getBoardContent()).isEqualTo(modifyBoardReq.getBoardContent());
        assertThat(findBoard.getAttachList().size()).isNotEqualTo(attachReqs.size());
        assertThat(findBoard.getAttachList().size()).isEqualTo(2);
    }
}