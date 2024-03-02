package com.puj.domain.board.service.dto;

import com.puj.domain.attachfile.AttachFile;
import com.puj.domain.board.Board;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.UUID;

@Getter
public class CreateAttachReq {
    private String originFilename;  // 실제 파일 명
    private String attachExtension; // 파일 확장자

    @Builder
    private CreateAttachReq(String originFilename, String attachExtension) {
        this.originFilename = originFilename;
        this.attachExtension = attachExtension;
    }
    // DTO 클래스를 Entity 로 변환
    public static AttachFile conversionAttachEntity(CreateAttachReq createAttachReq, Board board) {
        return AttachFile.builder()
                .attachPath("/Users/parksunghwan/uploadFile/")  // properties 활용하도록 리팩토링
                .saveFilename(UUID.randomUUID().toString())
                .originFilename(createAttachReq.getOriginFilename())
                .attachExtension(createAttachReq.getAttachExtension())
                .imageYN(checkImageType(createAttachReq.getAttachExtension()))
                .board(board)
                .build();
    }

    // 이미지 파일 체크
    private static String checkImageType(String attachExtension) {
        String[] imgExtension = {".jpeg", "jpg", ".gif", ".tiff", ".png", ".bmp", ".svg"};

        boolean imageResult = Arrays.stream(imgExtension).anyMatch(ext -> ext.equals(attachExtension));

        return imageResult ? "Y" : "N";
    }

}
