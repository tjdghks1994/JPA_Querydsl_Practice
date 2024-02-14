package com.puj.domain.board;

public enum BoardType {
    NOTICE("공지사항"), NORMAL("일반게시글"), QnA("QnA");

    private String typeValue;

    BoardType(String typeValue) {
        this.typeValue = typeValue;
    }
}
