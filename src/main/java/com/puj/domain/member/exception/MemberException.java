package com.puj.domain.member.exception;

public abstract class MemberException extends RuntimeException {
    public MemberException(final String message) {
        super(message);
    }
}
