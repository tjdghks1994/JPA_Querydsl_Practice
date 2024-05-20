package com.puj.domain.board.service.dto;

import com.puj.domain.attachfile.repository.dto.SearchAttachResp;
import com.puj.domain.board.Board;
import com.puj.domain.comment.repository.dto.SearchCommentResp;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SearchBoardResp {
    private final Long boardId;                         // 게시글 pk
    private final String boardTitle;                    // 게시글 제목
    private final String boardContent;                  // 게시글 내용
    private final int viewCnt;                          // 조회수
    private final LocalDateTime createdAt;              // 게시글 작성일자
    private final String writer;                        // 게시글 작성자 닉네임

    @Builder
    private SearchBoardResp(Long boardId, String boardTitle, String boardContent,
                           int viewCnt, LocalDateTime createdAt, String writer) {
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.viewCnt = viewCnt;
        this.createdAt = createdAt;
        this.writer = writer;
    }

    // Entity 와 연관관계가 있는 DTO 를 통해, 게시글 응답 DTO 변환
    public static SearchBoardResp toDTO(Board boardEntity) {
        return SearchBoardResp.builder()
                .boardId(boardEntity.getId())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContent(boardEntity.getBoardContent())
                .viewCnt(boardEntity.getViewCnt())
                .writer(boardEntity.getMember().getNickname())
                .build();
    }
}
