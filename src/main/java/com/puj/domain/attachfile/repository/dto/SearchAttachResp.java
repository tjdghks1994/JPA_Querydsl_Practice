package com.puj.domain.attachfile.repository.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchAttachResp {
    private final Long attachId;            // 첨부파일 pk
    private final String originFilename;    // 첨부파일 실제 파일명
    private final String attachExtension;   // 첨부파일 확장자
    private final String imageYN;           // 이미지 파일 여부

    @Builder
    public SearchAttachResp(Long attachId, String originFilename, String attachExtension, String imageYN) {
        this.attachId = attachId;
        this.originFilename = originFilename;
        this.attachExtension = attachExtension;
        this.imageYN = imageYN;
    }
}
