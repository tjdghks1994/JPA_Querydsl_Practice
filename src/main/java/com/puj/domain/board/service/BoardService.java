package com.puj.domain.board.service;

import com.puj.domain.attachfile.AttachFile;
import com.puj.domain.attachfile.repository.AttachFileRepository;
import com.puj.domain.attachfile.repository.dto.SearchAttachResp;
import com.puj.domain.board.Board;
import com.puj.domain.board.exception.InvalidBoardException;
import com.puj.domain.board.repository.BoardRepository;
import com.puj.domain.board.service.dto.CreateAttachReq;
import com.puj.domain.board.service.dto.CreateBoardReq;
import com.puj.domain.board.service.dto.SearchBoardResp;
import com.puj.domain.comment.Comment;
import com.puj.domain.comment.repository.CommentRepository;
import com.puj.domain.comment.repository.dto.SearchCommentResp;
import com.puj.domain.member.Member;
import com.puj.domain.member.exception.InvalidMemberException;
import com.puj.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final AttachFileRepository attachFileRepository;
    private final CommentRepository commentRepository;

    // 게시글 생성
    public Long createBoard(CreateBoardReq boardReq, Optional<CreateAttachReq> attachReq) {   // CreateBoardReq DTO 클래스의 필드 수정 필요 - 저장할 첨부파일 목록 정보 필드 추가 필요
        // 작성자 조회
        Member findMember = memberRepository.findByEmail(boardReq.getWriter())
                .orElseThrow(() -> new InvalidMemberException("존재하지 않는 사용자 입니다."));
        // 게시글 저장을 위한 Entity 생성
        Board boardEntity = Board.builder()
                .boardTitle(boardReq.getBoardTitle())
                .boardContent(boardReq.getBoardContent())
                .boardType(boardReq.getBoardType())
                .member(findMember)
                .build();
        // 게시글 저장
        Board saveBoard = boardRepository.save(boardEntity);

        // 첨부파일 저장을 위한 Entity 생성
        attachReq.ifPresent(attach -> {
            AttachFile attachEntity = AttachFile.builder()
                    .attachPath("/Users/parksunghwan/uploadFile/")  // properties 활용하도록 리팩토링
                    .saveFilename(UUID.randomUUID().toString())
                    .originFilename(attach.getOriginFilename())
                    .attachExtension(attach.getAttachExtension())
                    .imageYN(checkImageType(attach.getAttachExtension()))
                    .board(boardEntity)
                    .build();
            // 첨부파일 저장
            attachFileRepository.save(attachEntity);
        });

        return saveBoard.getId();
    }

    // 게시글 단건 조회
    public SearchBoardResp searchBoard(Long boardId) {
        // 게시글 조회
        Board findBoard = boardRepository.findBoardByIdWithMember(boardId)
                .orElseThrow(() -> new InvalidBoardException("존재하지 않는 게시글 입니다."));

        // 해당 게시글의 첨부파일 목록 조회
        List<SearchAttachResp> attachFileList = attachFileRepository.findAttachFileByBoardId(boardId)
                .stream()
                .map((a) -> SearchAttachResp.builder()
                                            .attachId(a.getId())
                                            .originFilename(a.getOriginFilename())
                                            .attachExtension(a.getAttachExtension())
                                            .imageYN(a.getImageYN())
                                            .build())
                .collect(Collectors.toList());

        // 해당 게시글의 댓글 목록 조회
        List<SearchCommentResp> commentList = commentRepository.findCommentByBoardId(boardId)
                .stream()
                .map((c) -> SearchCommentResp.builder()
                                            .commentId(c.getId())
                                            .commentContent(c.getContent())
                                            .createdAt(c.getCreatedAt())
                                            .parentCommentId(c.getParentComment().getId())
                                            .build()
                ).collect(Collectors.toList());

        SearchBoardResp resp = SearchBoardResp.builder()
                .boardId(findBoard.getId())
                .boardTitle(findBoard.getBoardTitle())
                .boardContent(findBoard.getBoardContent())
                .viewCnt(findBoard.getViewCnt())
                .writer(findBoard.getMember().getNickname())
                .attachList(attachFileList)
                .commentList(commentList)
                .build();

        return resp;
    }
    // 이미지 파일 체크
    private String checkImageType(String attachExtension) {
        String[] imgExtension = {".jpeg", "jpg", ".gif", ".tiff", ".png", ".bmp", ".svg"};

        boolean imageResult = Arrays.stream(imgExtension).anyMatch(ext -> ext.equals(attachExtension));

        return imageResult ? "Y" : "N";
    }
}
