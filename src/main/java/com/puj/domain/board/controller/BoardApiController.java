package com.puj.domain.board.controller;

import com.puj.domain.attachfile.repository.dto.CreateAttachReq;
import com.puj.domain.board.controller.dto.BoardAndAttachReq;
import com.puj.domain.board.facade.BoardFacade;
import com.puj.domain.board.service.dto.CreateBoardReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/jpa")
@RequiredArgsConstructor
public class BoardApiController {

    /**
     * 게시글 목록 조회    /boards                 GET
     * 게시글 등록        /boards                POST
     * 게시글 단건 조회    /boards/{boardId}      GET
     * 게시글 수정        /boards/{boardId}      PATCH
     * 게시글 삭제        /boards/{boardId}      DELETE
     */

    private final BoardFacade boardFacade;

    @PostMapping("/boards")
    public ResponseEntity<Long> createBoard(@RequestBody BoardAndAttachReq boardAndAttachReq) {
        log.info("boardAndAttachReq = {}", boardAndAttachReq);

        Long boardId = boardFacade
                .create(boardAndAttachReq.getCreateBoardReq(), boardAndAttachReq.getCreateAttachReqList());

        return ResponseEntity.created(URI.create("/jpa/boards/" + boardId)).build();
    }
}
