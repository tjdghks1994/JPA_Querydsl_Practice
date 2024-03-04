package com.puj.domain.board.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ModifyBoardReq {
    private Long boardId;           // 게시글 ID
    private String memberEmail;     // 작성자 이메일
    private String boardTitle;      // 게시글 제목
    private String boardContent;    // 게시글 내용

    @Builder
    private ModifyBoardReq(Long boardId, String memberEmail, String boardTitle, String boardContent) {
        this.boardId = boardId;
        this.memberEmail = memberEmail;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
