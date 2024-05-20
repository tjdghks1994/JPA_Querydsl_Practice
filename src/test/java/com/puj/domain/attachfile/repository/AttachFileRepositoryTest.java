package com.puj.domain.attachfile.repository;

import com.puj.domain.attachfile.AttachFile;
import com.puj.domain.board.Board;
import com.puj.domain.board.BoardType;
import com.puj.domain.board.repository.BoardRepository;
import com.puj.domain.member.Member;
import com.puj.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class AttachFileRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    AttachFileRepository attachFileRepository;

    @PersistenceContext
    EntityManager em;

    private Member member;
    private Board board;
    private AttachFile attachFile1;
    private AttachFile attachFile2;

    @BeforeEach
    @DisplayName("매 테스트마다 중복되는 엔티티들을 미리 세팅하기 위함")
    void initEntity() {
        member = Member.builder()
                .email("test@naver.com")
                .pwd("askdfeizxoci23lrn13lksl")
                .nickname("하디")
                .build();
        board = Board.builder()
                .boardTitle("게시글 제목")
                .boardContent("게시글 내용은 무수히 많은 문자열이 작성될 수 있습니다.")
                .boardType(BoardType.NORMAL)
                .member(member)
                .deleteYN("Y")
                .parentBoard(null)
                .build();
        attachFile1 = AttachFile.builder()
                .attachPath("/home/attach/")
                .saveFilename(UUID.randomUUID().toString())
                .originFilename("To-do List")
                .attachExtension(".txt")
                .imageYN("N")
                .board(board)
                .build();
        attachFile2 = AttachFile.builder()
                .attachPath("/home/attach/")
                .saveFilename(UUID.randomUUID().toString())
                .originFilename("To-do List2")
                .attachExtension(".txt")
                .imageYN("N")
                .board(board)
                .build();

        memberRepository.save(member);
        boardRepository.save(board);
        attachFileRepository.save(attachFile1);
        attachFileRepository.save(attachFile2);
    }

    @Test
    @DisplayName("게시글 첨부파일 목록 조회 테스트")
    void findAttachFileByBoardId() {
        List<AttachFile> findAttachList1 = attachFileRepository.findAttachFileByBoardId(board.getId());
        List<AttachFile> findAttachList2 = attachFileRepository.findAttachFileByBoardId(0L);

        assertThat(findAttachList1.size()).isEqualTo(2);
        assertThat(findAttachList2.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 첨부파일 단건 조회 테스트")
    void findAttachFileByIdAndBoardId() {
        Optional<AttachFile> findAttachFile1 = attachFileRepository.findAttachFileByIdAndBoardId(attachFile1.getId(), board.getId());
        Optional<AttachFile> findAttachFile2 = attachFileRepository.findAttachFileByIdAndBoardId(attachFile2.getId(), board.getId());

        assertThat(findAttachFile1.get()).isEqualTo(attachFile1);
        assertThat(findAttachFile2.get()).isEqualTo(attachFile2);
    }

    @Test
    @DisplayName("첨부파일 벌크 update 삭제 테스트 - deleteYN 값을 Y로 변경")
    void bulkAttachFileDelete() {
        List<Long> deleteAttachFileIds = Arrays.asList(attachFile1.getId(), attachFile2.getId());

        int resultCount = attachFileRepository.bulkAttachFileDelete(board.getId(), deleteAttachFileIds);
        // 벌크성 쿼리 실행 후 관련된 엔티티를 조회해야할 경우, 영속성 컨텍스트에 벌크성 쿼리 이전 상태의 값이 남아 있으므로
        // 관련된 엔티티를 조회할 경우에는 영속성 컨텍스트를 초기화!
        em.clear();
        em.close();

        Optional<AttachFile> findAttachFile1 = attachFileRepository.findAttachFileByIdAndBoardId(attachFile1.getId(), board.getId());
        Optional<AttachFile> findAttachFile2 = attachFileRepository.findAttachFileByIdAndBoardId(attachFile2.getId(), board.getId());

        assertThat(resultCount).isEqualTo(2);
        assertThat(findAttachFile1.get().getDeleteYN()).isEqualTo("Y");
        assertThat(findAttachFile2.get().getDeleteYN()).isEqualTo("Y");

    }
}