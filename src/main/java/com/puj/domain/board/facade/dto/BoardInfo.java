package com.puj.domain.board.facade.dto;

import com.puj.domain.attachfile.repository.dto.SearchAttachResp;
import com.puj.domain.board.service.dto.SearchBoardResp;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardInfo {
    private SearchBoardResp searchBoardResp;    // 게시글 관련 정보
    private List<SearchAttachResp> searchAttachRespList;   // 게시글 첨부파일 관련 정보 목록

    @Builder
    private BoardInfo(SearchBoardResp searchBoardResp, List<SearchAttachResp> searchAttachRespList) {
        this.searchBoardResp = searchBoardResp;
        this.searchAttachRespList = searchAttachRespList;
    }
}
