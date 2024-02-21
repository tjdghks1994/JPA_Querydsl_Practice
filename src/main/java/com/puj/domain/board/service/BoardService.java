package com.puj.domain.board.service;

import com.puj.domain.board.Board;
import com.puj.domain.board.repository.BoardRepository;
import com.puj.domain.board.service.dto.CreateBoardReq;
import com.puj.domain.member.Member;
import com.puj.domain.member.exception.InvalidMemberException;
import com.puj.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Long createBoard(CreateBoardReq req) {
        // 작성자 조회
        Member findMember = memberRepository.findByEmail(req.getWriter())
                .orElseThrow(() -> new InvalidMemberException("존재하지 않는 사용자 입니다."));
        // 게시글 저장을 위한 Entity 생성
        Board boardEntity = Board.builder()
                .boardTitle(req.getBoardTitle())
                .boardContent(req.getBoardContent())
                .boardType(req.getBoardType())
                .member(findMember)
                .build();
        // 저장
        Board saveBoard = boardRepository.save(boardEntity);

        return saveBoard.getId();
    }
}
