package com.example.springbootboard.error.exception;

public class NotAllowedAccessException extends BusinessException{
    public NotAllowedAccessException(String message) {
        super(message, ErrorCode.HANDLE_ACCESS_DENIED);
    }
}
