package com.puj.domain.board.exception;

public abstract class BoardException extends RuntimeException {
    public BoardException(String message) {
        super(message);
    }
}
