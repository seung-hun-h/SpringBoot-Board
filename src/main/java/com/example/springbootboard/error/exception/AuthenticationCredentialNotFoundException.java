package com.example.springbootboard.error.exception;

public class AuthenticationCredentialNotFoundException extends BusinessException {
    public AuthenticationCredentialNotFoundException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
}
