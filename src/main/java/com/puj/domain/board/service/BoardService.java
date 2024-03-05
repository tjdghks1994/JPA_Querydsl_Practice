package com.puj.domain.board.service;

import com.puj.domain.attachfile.AttachFile;
import com.puj.domain.attachfile.exception.NotFoundAttachFileException;
import com.puj.domain.attachfile.repository.AttachFileRepository;
import com.puj.domain.attachfile.repository.dto.SearchAttachResp;
import com.puj.domain.board.Board;
import com.puj.domain.board.exception.InvalidBoardException;
import com.puj.domain.board.exception.NotSameBoardWriterException;
import com.puj.domain.board.repository.BoardRepository;
import com.puj.domain.attachfile.repository.dto.CreateAttachReq;
import com.puj.domain.board.service.dto.CreateBoardReq;
import com.puj.domain.board.service.dto.ModifyBoardReq;
import com.puj.domain.board.service.dto.SearchBoardResp;
import com.puj.domain.comment.repository.CommentRepository;
import com.puj.domain.comment.repository.dto.SearchCommentResp;
import com.puj.domain.member.Member;
import com.puj.domain.member.exception.InvalidMemberException;
import com.puj.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public Long createBoard(CreateBoardReq boardReq, List<CreateAttachReq> attachReq) {
        // 작성자 조회
        Member findMember = memberRepository.findByEmail(boardReq.getWriter())
                .orElseThrow(() -> new InvalidMemberException("존재하지 않는 사용자 입니다."));
        // 게시글 저장을 위한 Entity 생성
        Board boardEntity = CreateBoardReq.conversionBoardEntity(boardReq, findMember);
        // 게시글 저장
        Board saveBoard = boardRepository.save(boardEntity);
        // 첨부파일 저장을 위한 Entity 생성
        if (attachReq != null) {
            attachReq.forEach(req -> {
                AttachFile attachEntity = CreateAttachReq.conversionAttachEntity(req, saveBoard);
                // 첨부파일 저장
                attachFileRepository.save(attachEntity);
            });
        }

        return saveBoard.getId();
    }

    // 게시글 단건 조회
    public SearchBoardResp readBoard(Long boardId) {
        // 게시글 조회
        Board findBoard = boardRepository.findBoardByIdWithMember(boardId)
                .orElseThrow(() -> new InvalidBoardException("존재하지 않는 게시글 입니다."));

        // 해당 게시글의 첨부파일 목록 조회
        List<SearchAttachResp> attachFileList = attachFileRepository.findAttachFileByBoardId(boardId)
                .stream()
                .map((attachFile) -> SearchAttachResp.toDTO(attachFile))
                .collect(Collectors.toList());

        // 해당 게시글의 댓글 목록 조회
        List<SearchCommentResp> commentList = commentRepository.findCommentByBoardId(boardId)
                .stream()
                .map((comment) -> SearchCommentResp.toDTO(comment))
                .collect(Collectors.toList());

        SearchBoardResp resp = SearchBoardResp.toDTO(findBoard, attachFileList, commentList);

        return resp;
    }

    // 게시글 수정
    public Long updateBoard(ModifyBoardReq modifyBoardReq,
                                       List<Long> deleteAttachIds,
                                       List<CreateAttachReq> newAttachList) {
        // 수정하려는 게시글 엔티티 조회
        Board findBoard = boardRepository.findBoardByIdWithMember(modifyBoardReq.getBoardId())
                .orElseThrow(() -> new InvalidBoardException("존재하지 않는 게시글 입니다."));
        // 게시글을 수정하려는 사람이 게시글 작성자가 맞는지 체크
        checkWriter(findBoard.getMember().getEmail(), modifyBoardReq.getMemberEmail());
        // 게시글 수정 - 변경 감지 활용
        findBoard.changeBoardInfo(modifyBoardReq.getBoardTitle(), modifyBoardReq.getBoardContent());
        // 삭제할 첨부파일 목록이 존재한다면 삭제
        if (deleteAttachIds != null) {
            deleteAttachIds.stream().forEach((attachId) -> {
                AttachFile attachFile = attachFileRepository.findAttachFileByIdAndBoardId(attachId, findBoard.getId())
                        .orElseThrow(() -> new NotFoundAttachFileException("게시글에 존재하지 않는 첨부파일 입니다."));
            });
            // 첨부파일 삭제 처리 - deleteYN 값을 Y로 변경
            attachFileRepository.bulkAttachFileDelete(findBoard.getId(), deleteAttachIds);
        }
        // 새로운 첨부파일 목록이 존재한다면 추가
        if (newAttachList != null) {
            newAttachList.stream().forEach((newAttach) -> {
                AttachFile attachFile = CreateAttachReq.conversionAttachEntity(newAttach, findBoard);
                attachFileRepository.save(attachFile);
            });
        }

        return findBoard.getId();
    }

    // 게시글 삭제


    // 게시글을 수정하려는 사람이 게시글 작성자가 맞는지 체크
    private void checkWriter(String boardWriter, String boardModifier) {
        if (!boardWriter.equals(boardModifier)) {
            throw new NotSameBoardWriterException("게시글 작성자가 아닙니다.");
        }
    }
}
