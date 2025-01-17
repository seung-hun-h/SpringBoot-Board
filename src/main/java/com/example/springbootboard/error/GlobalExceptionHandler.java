package com.example.springbootboard.error;

import com.example.springbootboard.error.exception.BusinessException;
import com.example.springbootboard.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @RequestBody binding error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // @ModelAttribute binding error
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // enum type mismatch error
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        ErrorResponse errorResponse = ErrorResponse.of(e);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 지원하지 않는 HTTP Method error
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleAccessDeniedException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    // Business error
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("handleBusinessException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);

        return ResponseEntity.internalServerError().body(errorResponse);
    }

    // etc runtime exception
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error("handleRuntimeException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);

        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
