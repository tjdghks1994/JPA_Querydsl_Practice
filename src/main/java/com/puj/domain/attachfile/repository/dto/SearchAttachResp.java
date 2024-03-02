package com.puj.domain.attachfile.repository.dto;

import com.puj.domain.attachfile.AttachFile;
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

    // Entity 를 DTO 로 변환
    public static SearchAttachResp toDTO(AttachFile attachFile) {
        return SearchAttachResp.builder()
                .attachId(attachFile.getId())
                .originFilename(attachFile.getOriginFilename())
                .attachExtension(attachFile.getAttachExtension())
                .imageYN(attachFile.getImageYN())
                .build();
    }
}
