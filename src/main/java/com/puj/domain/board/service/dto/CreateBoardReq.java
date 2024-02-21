package com.puj.domain.board.service.dto;

import com.puj.domain.board.BoardType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateBoardReq {
    private final String writer;        // 작성자 이메일아이디
    private final String boardTitle;    // 게시글제목
    private final String boardContent;  // 게시글내용
    private final BoardType boardType;  // 게시글타입

    @Builder
    public CreateBoardReq(String boardTitle, String boardContent,
                          BoardType boardType, String writer) {
        this.writer = writer;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardType = boardType;
    }
}
