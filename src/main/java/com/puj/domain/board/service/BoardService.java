package com.puj.domain.board.service;

import com.puj.domain.board.Board;
import com.puj.domain.board.exception.InvalidBoardException;
import com.puj.domain.board.exception.NotSameBoardWriterException;
import com.puj.domain.board.repository.BoardRepository;
import com.puj.domain.board.service.dto.CreateBoardReq;
import com.puj.domain.board.service.dto.ModifyBoardReq;
import com.puj.domain.board.service.dto.SearchBoardResp;
import com.puj.domain.member.Member;
import com.puj.domain.member.exception.RequiredMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 생성
    public Long createBoard(CreateBoardReq boardReq, Optional<Member> member) {
        // 게시글 저장을 위한 Entity 생성
        Board boardEntity = CreateBoardReq
                .conversionBoardEntity(boardReq, member.orElseThrow(() ->
                        new RequiredMemberException("게시글 작성을 위해서는 작성자는 필수입니다.")));
        // 게시글 저장
        Board saveBoard = boardRepository.save(boardEntity);

        return saveBoard.getId();
    }

    // 게시글 단건 조회
    public SearchBoardResp readBoard(Long boardId) {
        // 게시글 조회
        Board findBoard = findBoardByIdWithMemberEntity(boardId);

        SearchBoardResp resp = SearchBoardResp.toDTO(findBoard);

        return resp;
    }

    // 게시글 수정
    public Long updateBoard(ModifyBoardReq modifyBoardReq) {
        // 수정하려는 게시글 엔티티 조회
        Board findBoard = findBoardByIdWithMemberEntity(modifyBoardReq.getBoardId());
        // 게시글을 수정하려는 사람이 게시글 작성자가 맞는지 체크
        checkWriter(findBoard.getMember().getEmail(), modifyBoardReq.getMemberEmail());
        // 게시글 수정 - 변경 감지 활용
        findBoard.changeBoardInfo(modifyBoardReq.getBoardTitle(), modifyBoardReq.getBoardContent());

        return findBoard.getId();
    }

    // 게시글 삭제
    public void removeBoard(DeleteBoardReq deleteBoardReq) {
        // 삭제하려는 게시글 엔티티 조회
        Board removeBoard = findBoardByIdWithMemberEntity(deleteBoardReq.getBoardId());
        // 삭제하려는 사람이 게시글 작성자가 맞는지 확인
        checkWriter(removeBoard.getMember().getEmail(), deleteBoardReq.getWriter());
        // 게시글 삭제 (soft delete) - 변경 감지 활용
        removeBoard.removeBoard();
    }

    // 게시글 조회
    private Board findBoardByIdWithMemberEntity(Long boardId) {
        Board findBoard = boardRepository.findBoardByIdWithMember(boardId)
                .orElseThrow(() -> new InvalidBoardException("존재하지 않는 게시글 입니다."));
        return findBoard;
    }


    // 게시글 작성자가 맞는지 체크
    private void checkWriter(String boardWriter, String requester) {
        if (!boardWriter.equals(requester)) {
            throw new NotSameBoardWriterException("게시글 작성자가 아닙니다.");
        }
    }
}
