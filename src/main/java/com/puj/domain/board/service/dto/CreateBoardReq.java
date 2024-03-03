package com.puj.domain.board.service.dto;

import com.puj.domain.board.Board;
import com.puj.domain.board.BoardType;
import com.puj.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateBoardReq {
    private final String writer;        // 작성자 이메일아이디
    private final String boardTitle;    // 게시글제목
    private final String boardContent;  // 게시글내용
    private final BoardType boardType;  // 게시글타입

    @Builder
    private CreateBoardReq(String boardTitle, String boardContent,
                          BoardType boardType, String writer) {
        this.writer = writer;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardType = boardType;
    }
    // DTO 클래스를 Entity 로 변환
    public static Board conversionBoardEntity(CreateBoardReq createBoardReq, Member member) {
        return Board.builder()
                .boardTitle(createBoardReq.getBoardTitle())
                .boardContent(createBoardReq.getBoardContent())
                .boardType(createBoardReq.getBoardType())
                .member(member)
                .build();
    }
}
