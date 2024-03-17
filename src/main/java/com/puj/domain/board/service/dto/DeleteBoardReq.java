package com.puj.domain.board.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteBoardReq {
    private Long boardId;
    private String writer;

    @Builder
    private DeleteBoardReq(Long boardId, String writer) {
        this.boardId = boardId;
        this.writer = writer;
    }
}
