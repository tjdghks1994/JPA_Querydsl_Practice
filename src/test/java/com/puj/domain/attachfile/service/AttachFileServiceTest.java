package com.puj.domain.attachfile.service;

import com.puj.domain.attachfile.AttachFile;
import com.puj.domain.attachfile.exception.NotFoundAttachFileException;
import com.puj.domain.attachfile.repository.dto.CreateAttachReq;
import com.puj.domain.attachfile.repository.dto.SearchAttachResp;
import com.puj.domain.board.Board;
import com.puj.domain.board.BoardType;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class AttachFileServiceTest {

    @Autowired
    private AttachFileService service;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("첨부파일 저장 / 조회 성공 테스트")
    void saveTest() {
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
        em.persist(member);
        em.persist(board);
        CreateAttachReq attachReq1 = CreateAttachReq.builder()
                .originFilename("test")
                .attachExtension(".txt")
                .build();
        CreateAttachReq attachReq2 = CreateAttachReq.builder()
                .originFilename("test2")
                .attachExtension(".txt")
                .build();
        List<CreateAttachReq> createAttachReqList = Arrays.asList(attachReq1, attachReq2);

        service.saveAttachFileList(createAttachReqList, board);
        List<SearchAttachResp> searchAttachResps = service.searchAttachFileList(board.getId());

        Assertions.assertThat(createAttachReqList.size()).isEqualTo(searchAttachResps.size());
    }

    @Test
    @DisplayName("첨부파일 목록 삭제 성공 테스트 - soft delete")
    void removeAttachFileListTest() {
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
        em.persist(member);
        em.persist(board);
        CreateAttachReq attachReq1 = CreateAttachReq.builder()
                .originFilename("test")
                .attachExtension(".txt")
                .build();
        CreateAttachReq attachReq2 = CreateAttachReq.builder()
                .originFilename("test2")
                .attachExtension(".txt")
                .build();
        List<CreateAttachReq> createAttachReqList = Arrays.asList(attachReq1, attachReq2);

        service.saveAttachFileList(createAttachReqList, board);
        List<SearchAttachResp> searchAttachResps = service.searchAttachFileList(board.getId());
        List<Long> deleteAttachFileIds = searchAttachResps.stream().map(searchAttachResp -> searchAttachResp.getAttachId())
                .collect(Collectors.toList());

        service.removeAttachFileList(deleteAttachFileIds, board.getId());

        em.flush();
        em.clear();
        em.close();

        List<AttachFile> attachFiles = em.createQuery("select a from AttachFile a join a.board b where b.id = :boardId", AttachFile.class)
                .setParameter("boardId", board.getId())
                .getResultList();

        Assertions.assertThat(attachFiles.get(0).getDeleteYN()).isEqualTo("Y");
        Assertions.assertThat(attachFiles.get(1).getDeleteYN()).isEqualTo("Y");
    }

    @Test
    @DisplayName("첨부파일 목록 삭제 실패 테스트 - soft delete")
    void removeAttachFileListFailTest() {
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
        em.persist(member);
        em.persist(board);
        CreateAttachReq attachReq1 = CreateAttachReq.builder()
                .originFilename("test")
                .attachExtension(".txt")
                .build();
        List<CreateAttachReq> createAttachReqList = Arrays.asList(attachReq1);
        service.saveAttachFileList(createAttachReqList, board);

        List<Long> deleteAttachFileIds = Arrays.asList(0L);

        Assertions.assertThatThrownBy(() -> service.removeAttachFileList(deleteAttachFileIds, board.getId()))
                .isInstanceOf(NotFoundAttachFileException.class);

    }
}