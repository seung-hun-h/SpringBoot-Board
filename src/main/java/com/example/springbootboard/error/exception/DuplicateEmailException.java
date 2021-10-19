package com.example.springbootboard.error.exception;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException(String message) {
        super(message, ErrorCode.DUPLICATE_EMAIL);
    }
}
