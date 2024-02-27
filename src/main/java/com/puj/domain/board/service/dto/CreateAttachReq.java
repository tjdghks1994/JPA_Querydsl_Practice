package com.puj.domain.board.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateAttachReq {
    private String originFilename;  // 실제 파일 명
    private String attachExtension; // 파일 확장자

    @Builder
    private CreateAttachReq(String originFilename, String attachExtension) {
        this.originFilename = originFilename;
        this.attachExtension = attachExtension;
    }

}
