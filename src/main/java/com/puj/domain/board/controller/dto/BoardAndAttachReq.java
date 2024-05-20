package com.puj.domain.board.controller.dto;


import com.puj.domain.attachfile.repository.dto.CreateAttachReq;
import com.puj.domain.board.service.dto.CreateBoardReq;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@ToString
@NoArgsConstructor(access = PRIVATE)
public class BoardAndAttachReq {

    private CreateBoardReq createBoardReq;
    private List<CreateAttachReq> createAttachReqList = new ArrayList<>();  // 게시글 등록시, 첨부파일이 존재하지 않을 때 NPE 방지
}
