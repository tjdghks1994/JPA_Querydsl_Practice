package com.puj.domain.board.facade;

import com.puj.domain.attachfile.repository.dto.CreateAttachReq;
import com.puj.domain.attachfile.service.AttachFileService;
import com.puj.domain.board.Board;
import com.puj.domain.board.service.BoardService;
import com.puj.domain.board.service.dto.CreateBoardReq;
import com.puj.domain.comment.service.CommentService;
import com.puj.domain.member.Member;
import com.puj.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class BoardFacade {

    private final MemberService memberService;
    private final BoardService boardService;
    private final AttachFileService attachFileService;
    private final CommentService commentService;

    // 게시글 생성(저장)
    public Long create(CreateBoardReq createBoardReq, List<CreateAttachReq> createAttachReqList) {
        // 회원 존재여부 체크
        Member member = memberService.searchMemberByEmail(createBoardReq.getWriter());
        // 게시글 엔티티 생성
        Board createBoard = boardService.createBoard(createBoardReq, member);
        // 첨부파일 엔티티 생성
        attachFileService.saveAttachFileList(createAttachReqList, createBoard);

        return createBoard.getId();
    }
}
