package com.puj.domain.attachfile.repository;

import com.puj.domain.attachfile.AttachFile;
import com.puj.domain.board.Board;
import com.puj.domain.board.BoardType;
import com.puj.domain.board.repository.BoardRepository;
import com.puj.domain.member.Member;
import com.puj.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Test
    void findAttachFileByBoardId() {
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
        AttachFile attachFile = AttachFile.builder()
                .attachPath("/home/attach/")
                .saveFilename(UUID.randomUUID().toString())
                .originFilename("To-do List")
                .attachExtension(".txt")
                .imageYN("N")
                .board(newBoard)
                .build();
        AttachFile attachFile2 = AttachFile.builder()
                .attachPath("/home/attach/")
                .saveFilename(UUID.randomUUID().toString())
                .originFilename("To-do List2")
                .attachExtension(".txt")
                .imageYN("N")
                .board(newBoard)
                .build();

        Member saveMember = memberRepository.save(member);
        Board saveBoard = boardRepository.save(newBoard);
        AttachFile saveFile1 = attachFileRepository.save(attachFile);
        AttachFile saveFile2 = attachFileRepository.save(attachFile2);

        List<AttachFile> attachFileList = attachFileRepository.findAttachFileByBoardId(saveBoard.getId());
        List<AttachFile> attachFileList2 = attachFileRepository.findAttachFileByBoardId(0L);

        assertThat(attachFileList.size()).isEqualTo(2);
        assertThat(attachFileList2.size()).isEqualTo(0);
    }
}